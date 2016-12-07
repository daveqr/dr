/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Loads source strings from an in-memory database.
 * 
 * @since 1.0.0
 */
@Repository( "dbSourceDao" )
public class DbSourceDao implements DbDao, SourceDao {

   private JdbcTemplate jdbcTemplate;

   @Autowired
   public void setJdbcTemplate( JdbcTemplate jdbcTemplate ) {
      this.jdbcTemplate = jdbcTemplate;
   }


   @Override
   public void insertClob( int id, String longtext ) {
      String sql = "update documents set longtext=? where id=?";

      jdbcTemplate.update( sql, longtext, id );
   }


   @Override
   public List<String> loadSourceStrings() throws Exception {
      String sql = "SELECT longtext FROM documents";

      return jdbcTemplate.queryForList( sql, String.class );
   }
}