package kadai_004;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Employees_Chapter04 {
    public static void main(String[] args) {

        Connection con = null;
        Statement statement = null;

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "Himawasa3007#"
            );

            System.out.println("データベース接続成功");

            // SQLクエリを準備
            statement = con.createStatement();
            String sql = """
                         CREATE TABLE employees (
                           id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 社員ID（自動インクリメント、主キー）
                           name VARCHAR(60) NOT NULL,                       -- 氏名（最大60文字、NULL不可）
                           email VARCHAR(255) NOT NULL,                    -- メールアドレス（最大255文字、NULL不可）
                           age INT(11),                                    -- 年齢（NULL可）
                           address VARCHAR(255)                            -- 住所（最大255文字、NULL可）
                         );
                         """;

            // SQLクエリを実行（DBMSに送信）
            int rowCnt = statement.executeUpdate(sql);
            System.out.println("テーブルを作成:rowCnt=" + rowCnt);
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if (statement != null) {
                try {
                    statement.close();
                } catch(SQLException ignore) {}
            }
            if (con != null) {
                try {
                    con.close();
                } catch(SQLException ignore) {}
            }
        }
    }
}
