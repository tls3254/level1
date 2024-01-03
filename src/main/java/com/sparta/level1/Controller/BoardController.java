package com.sparta.level1.Controller;

import com.sparta.level1.dto.BoardResponseDto;
import com.sparta.level1.dto.BoardRequestDto;
import com.sparta.level1.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {
    private final JdbcTemplate jdbcTemplate;
    public BoardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //게시글 작성 기능
    @PostMapping("/board")
    public BoardResponseDto createboard(@RequestBody BoardRequestDto requestDto){
        Board board =new Board(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO board (title, username, password, contents, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getTitle());
                    preparedStatement.setString(2, board.getUsername());
                    preparedStatement.setString(3, board.getPassword());
                    preparedStatement.setString(4, board.getContents());
                    preparedStatement.setString(5, board.getDate());
                    return preparedStatement;
                },
                keyHolder);


        Long id = keyHolder.getKey().longValue();
        board.setId(id);

        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    //선택한 게시글 조회 기능
    @GetMapping("/board/{id}")
    public List<BoardResponseDto> getSelectBoard(@PathVariable Long id){
        String sql ="SELECT * FROM board where id =?";
        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("Username");
                String contents = rs.getString("contents");
                String date = rs.getString("date");
                return new BoardResponseDto(id, title, username, contents, date);
            }
        },id);
    }

    //게시글 목록 조회 기능
    @GetMapping("/board")
    public List<BoardResponseDto> getBoard(){
        String sql ="SELECT * FROM board";
        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("Username");
                String contents = rs.getString("contents");
                String date = rs.getString("date");
                return new BoardResponseDto(id, title, username, contents, date);
            }
        });
    }

    //선택한 게시글 수정 기능
    @PutMapping("/board/{id}")
    public Long updateBoard(@PathVariable Long id,@RequestParam String password, @RequestBody BoardRequestDto requestDto) {
        // 해당 board가 DB에 존재하는지 확인
        Board board = findById(id,password);
        if(board != null) {
            // board 내용 수정
            String sql = "UPDATE board SET title = ?, username =?,contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUsername(),requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    //선택한 게시글 삭제 기능
    @DeleteMapping("/board/{id}")
    public Long deleteBoard(@PathVariable Long id,@RequestParam String password) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id,password);
        if(board != null) {
            // memo 삭제
            String sql = "DELETE FROM board WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시판은 존재하지 않습니다.");
        }
    }

    private Board findById(Long id,String password) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ? and password =?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setTitle(resultSet.getString("title"));
                board.setUsername(resultSet.getString("username"));
                board.setContents(resultSet.getString("contents"));
                return board;
            } else {
                return null;
            }
        }, id,password);
    }
}
