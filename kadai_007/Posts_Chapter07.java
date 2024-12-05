package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {

    public static void main(String[] args) {
        Connection con = null;

        // 投稿データリスト
        String[][] postList = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
            { "1002", "2023-02-08", "お疲れ様です！", "12" },
            { "1003", "2023-02-09", "今日も頑張ります！", "18" },
            { "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
            { "1002", "2023-02-10", "明日から連休ですね！", "20" }
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java", // challenge_java を使用
                "root",
                "Himawasa3007#" // パスワードを適宜変更
            );

            // 接続成功メッセージ
            System.out.println("データベース接続成功：" + con);

            // データを挿入する
            insertPosts(con, postList);

            // ユーザーIDが1002の投稿を検索する
            searchPostsByUserId(con, 1002);

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * postsテーブルにデータを挿入するメソッド
     */
    private static void insertPosts(Connection con, String[][] postList) {
        PreparedStatement statement = null;
        String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
        try {
            statement = con.prepareStatement(sql);
            System.out.println("レコード追加を実行します");

            for (String[] post : postList) {
                statement.setInt(1, Integer.parseInt(post[0])); // user_id
                statement.setString(2, post[1]); // posted_at
                statement.setString(3, post[2]); // post_content
                statement.setInt(4, Integer.parseInt(post[3])); // likes
                statement.executeUpdate();
            }

            System.out.println(postList.length + "件のレコードが追加されました");
        } catch (SQLException e) {
            System.out.println("データ挿入中にエラーが発生しました：" + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * 特定のユーザーIDの投稿を検索するメソッド
     */
    private static void searchPostsByUserId(Connection con, int userId) {
        PreparedStatement statement = null;
        String sql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = ?;";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();

            System.out.println("ユーザーIDが" + userId + "のレコードを検索しました");
            int recordCount = 0;

            while (result.next()) {
                recordCount++;
                String postedAt = result.getDate("posted_at").toString();
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(recordCount + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
            }
        } catch (SQLException e) {
            System.out.println("データ検索中にエラーが発生しました：" + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
