package kadai_010;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Scores_Chapter11 {

    public static void main(String[] args) {
        Connection con = null;

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java", // challenge_java データベースに接続
                "root", // MySQLユーザー名
                "Himawasa3007#" // MySQLパスワード
            );

            System.out.println("データベース接続成功：" + con);

            // Step3: scoresテーブルの特定レコードを更新
            updateScores(con, 5, 95, 80); // id=5（武者小路勇気さん）の点数を更新

            // Step4: scoresテーブルの全レコードを取得し、並べ替えて表示
            displaySortedScores(con);

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close(); // データベース接続を閉じる
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Step3: scoresテーブルの特定の生徒の点数を更新するメソッド
     * 
     * @param con データベース接続オブジェクト
     * @param id 更新対象の生徒ID
     * @param mathScore 新しい数学の得点
     * @param englishScore 新しい英語の得点
     */
    private static void updateScores(Connection con, int id, int mathScore, int englishScore) {
        PreparedStatement statement = null;
        String sql = "UPDATE scores SET score_math = ?, score_english = ? WHERE id = ?;";

        try {
            statement = con.prepareStatement(sql);

            // プレースホルダーに値をセット
            statement.setInt(1, mathScore); // 1番目の「?」に数学の点数をセット
            statement.setInt(2, englishScore); // 2番目の「?」に英語の点数をセット
            statement.setInt(3, id); // 3番目の「?」に更新対象の生徒IDをセット

            int rowsUpdated = statement.executeUpdate(); // SQLを実行してデータを更新
            System.out.println(rowsUpdated + "件のレコードが更新されました");

        } catch (SQLException e) {
            System.out.println("データ更新中にエラーが発生しました：" + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close(); // ステートメントを閉じる
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Step4: scoresテーブルの全レコードを取得し、点数順に並べ替えて表示するメソッド
     * 
     * @param con データベース接続オブジェクト
     */
    private static void displaySortedScores(Connection con) {
        PreparedStatement statement = null;
        // SQL文: 数学の点数が高い順、次に英語の点数が高い順に並べ替える
        String sql = "SELECT id, name, score_math, score_english FROM scores "
                   + "ORDER BY score_math DESC, score_english DESC;";

        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            System.out.println("数学・英語の点数が高い順に並べ替えました");
            int recordCount = 0;

            // 結果を1件ずつ表示
            while (result.next()) {
                recordCount++; // レコード番号をカウント
                int id = result.getInt("id"); // 生徒ID
                String name = result.getString("name"); // 氏名
                int mathScore = result.getInt("score_math"); // 数学の得点
                int englishScore = result.getInt("score_english"); // 英語の得点

                // レコードを指定された形式で出力
                System.out.printf("%d件目：生徒ID=%d／氏名=%s／数学=%d／英語=%d\n",
                        recordCount, id, name, mathScore, englishScore);
            }

        } catch (SQLException e) {
            System.out.println("データ取得中にエラーが発生しました：" + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close(); // ステートメントを閉じる
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
