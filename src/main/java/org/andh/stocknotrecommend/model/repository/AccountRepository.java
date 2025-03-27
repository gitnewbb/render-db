package org.andh.stocknotrecommend.model.repository;

import io.github.cdimascio.dotenv.Dotenv;
import org.andh.stocknotrecommend.model.dto.Account;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class AccountRepository implements JDBCRepository{
    final Dotenv dotenv=Dotenv.configure().ignoreIfMissing().load();
    final String url=dotenv.get("DB_URL");
    final String user=dotenv.get("DB_USER");
    final String password=dotenv.get("DB_PASSWORD");
    final String superpassword=dotenv.get("SUPER_PASSWORD");


    public List<Account> findAll() throws Exception {
        List<Account> posts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";
        try (Connection conn = getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(new Account(
                        rs.getLong("account_id"),
                        rs.getString("nickname"),
                        rs.getString("password"),   // 원하면 이건 빼도 됨
                        rs.getString("title"),
                        rs.getString("body_data"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("recommended"),
                        rs.getInt("notrecommended")
                ));
            }
        }
        return posts;
    }

    public void save(Account account) throws Exception {
        String sql = "INSERT INTO accounts (nickname, password, title, body_data) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.nickname());
            pstmt.setString(2, account.password());
            pstmt.setString(3, account.title());
            pstmt.setString(4, account.body_data());
            pstmt.executeUpdate();
        }
    }

    public boolean unsubscribe(Long accountId, String ownpassword) throws Exception {
        boolean useSuper = Objects.equals(ownpassword, superpassword);

        String sql;
        if (useSuper) {
            sql = "DELETE FROM accounts WHERE account_id = ?";
        } else {
            sql = "DELETE FROM accounts WHERE account_id = ? AND password = ?";
        }

        try (Connection conn = getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, accountId);
            if (!useSuper) {
                pstmt.setString(2, ownpassword);
            }

            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }
    public boolean vote(Long accountId, String ip, boolean isRecommend) throws Exception {
        // 먼저 vote_log에 기록이 있는지 확인
        String checkSql = "SELECT COUNT(*) FROM vote_log WHERE account_id = ? AND ip_address = ?";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setLong(1, accountId);
            checkStmt.setString(2, ip);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // 이미 추천/비추천한 이력이 있음
            }
        }

        // 추천/비추천 카운트 증가
        String updateSql = "UPDATE accounts SET " +
                (isRecommend ? "recommended = recommended + 1" : "notrecommended = notrecommended + 1") +
                " WHERE account_id = ?";
        String logSql = "INSERT INTO vote_log (account_id, ip_address, action) VALUES (?, ?, ?)";

        try (Connection conn = getConnection(url, user, password)) {
            conn.setAutoCommit(false); // 트랜잭션

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                 PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                updateStmt.setLong(1, accountId);
                updateStmt.executeUpdate();

                logStmt.setLong(1, accountId);
                logStmt.setString(2, ip);
                logStmt.setString(3, isRecommend ? "RECOMMEND" : "NOT_RECOMMEND");
                logStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }


    private Account fromResultSet(ResultSet rs) throws SQLException {
        return new Account(
                rs.getLong("account_id"),
                rs.getString("nickname"),
                rs.getString("password"),
                rs.getString("title"),
                rs.getString("body_data"),
                rs.getTimestamp("created_at"),
                rs.getInt("recommended"),
                rs.getInt("notrecommended")
        );
    }

    public List<Account> findTop3ByRecommend() throws Exception {
        List<Account> result = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY recommended DESC LIMIT 3";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.add(fromResultSet(rs));
            }
        }
        return result;
    }
    public List<Account> findByPage(int page, int pageSize) throws Exception {
        List<Account> result = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(fromResultSet(rs));
                }
            }
        }
        return result;
    }
    public int countTotalPages(int pageSize) throws Exception {
        String sql = "SELECT COUNT(*) FROM accounts";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int totalCount = rs.getInt(1);
                return (int) Math.ceil((double) totalCount / pageSize);
            }
        }
        return 1;
    }








}
