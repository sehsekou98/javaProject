package com.sekou.springbootproject.customers;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
       ResultSet resultSet = mock(ResultSet.class);
       when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Kalasco");
        when(resultSet.getString("email")).thenReturn("kalasco@gmail.com");
        when(resultSet.getInt("age")).thenReturn(18);

        Customer actual = (Customer) customerRowMapper.mapRow(resultSet,1);



        Customer expected = new Customer(
                1, "Kala-sco", "kalasco@gmail.com", 18
        );
        assertThat(actual).isEqualTo(expected);
    }
}