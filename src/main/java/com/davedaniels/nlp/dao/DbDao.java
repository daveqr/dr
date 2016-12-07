/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

/**
 * Updates in-memory database with a long string of text.
 * 
 * @since 1.0.0
 */
public interface DbDao {

   void insertClob( int id, String clob );

}