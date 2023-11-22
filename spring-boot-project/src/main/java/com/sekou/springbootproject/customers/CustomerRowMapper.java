package com.sekou.springbootproject.customers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class CustomerRowMapper implements RowMapper{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
        );
        return customer;
    }
}
