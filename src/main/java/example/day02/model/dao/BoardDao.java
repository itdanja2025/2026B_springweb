package example.day02.model.dao;

public class BoardDao extends BaseDao {
    private BoardDao(){};
    private static final BoardDao instance = new BoardDao();
    public static BoardDao getInstance( ){ return instance; }
}
