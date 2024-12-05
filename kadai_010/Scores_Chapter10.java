package kadai_010;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Scores_Chapter10 {

	public static void main(String[] args) {
		Connection con = null;

		try {
			// データベースに接続
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/challenge_java",
					"root",
					"Himawasa3007#");

			System.out.println("データベース接続成功：" + con);

			// 点数を更新
			updateScores(con, 5, 95, 80);

			// 並べ替え結果を表示
			displaySortedScores(con);

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

	private static void updateScores(Connection con, int id, int mathScore, int englishScore) {
		String sql = "UPDATE scores SET score_math = ?, score_english = ? WHERE id = ?;";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setInt(1, mathScore);
			statement.setInt(2, englishScore);
			statement.setInt(3, id);
			int rowsUpdated = statement.executeUpdate();
			System.out.println(rowsUpdated + "件のレコードが更新されました");
		} catch (SQLException e) {
			System.out.println("データ更新中にエラーが発生しました：" + e.getMessage());
		}
	}

	private static void displaySortedScores(Connection con) {
		String sql = "SELECT id, name, score_math, score_english FROM scores ORDER BY score_math DESC, score_english DESC;";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			ResultSet result = statement.executeQuery();
			System.out.println("数学・英語の点数が高い順に並べ替えました");
			int recordCount = 0;
			while (result.next()) {
				recordCount++;
				System.out.printf("%d件目：生徒ID=%d／氏名=%s／数学=%d／英語=%d\n",
						recordCount, result.getInt("id"), result.getString("name"),
						result.getInt("score_math"), result.getInt("score_english"));
			}
		} catch (SQLException e) {
			System.out.println("データ取得中にエラーが発生しました：" + e.getMessage());
		}
	}
}
