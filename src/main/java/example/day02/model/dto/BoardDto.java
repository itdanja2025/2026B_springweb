package example.day02.model.dto;

public class BoardDto {
    private int no;
    private String content;
    private String writer;
    public BoardDto() { }
    public BoardDto(int no, String content, String writer) {
        this.no = no;
        this.content = content;
        this.writer = writer;
    }
    // 3. setter and getter , toString 
    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getWriter() {
        return writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }
    @Override
    public String toString() {
        return "BoardDto [no=" + no + ", content=" + content + ", writer=" + writer + "]";
    }
} // CLASS END 
