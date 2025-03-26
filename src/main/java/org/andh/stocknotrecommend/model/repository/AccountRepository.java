package org.andh.stocknotrecommend.model.repository;

import io.github.cdimascio.dotenv.Dotenv;
import org.andh.stocknotrecommend.model.dto.Account;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository implements JDBCRepository{
    final Dotenv dotenv=Dotenv.configure().ignoreIfMissing().load();
    final String url=dotenv.get("DB_URL");
    final String user=dotenv.get("DB_USER");
    final String password=dotenv.get("DB_PASSWORD");
    public List<Account> findAll() throws Exception {
        List<Account> accounts=new ArrayList<>();
        try(Connection conn=getConnection(url,user,password)) {
            Statement stmt = conn.createStatement();
            String query="SELECT * FROM accounts ORDER BY account_id ASC";
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()){
                accounts.add(new Account(rs.getLong("account_id"), rs.getString("nickname")));
            }
        }
        return accounts;
    }
    public void save(Account account) throws Exception {
        try(Connection conn=getConnection(url,user,password)) {
            Statement stmt = conn.createStatement();
            String query="INSERT INTO accounts (nickname) VALUES ('%s')".formatted(account.nickname());
            stmt.executeUpdate(query);
        }
    }
    public void delete(long id) throws Exception {
        try(Connection conn=getConnection(url,user,password)) {
            Statement stmt = conn.createStatement();
            String query="DELETE FROM accounts WHERE account_id = %d".formatted(id);
            int rowsAffected=stmt.executeUpdate(query);
            System.out.println("rowsAffected="+rowsAffected);
        }
    }

}
