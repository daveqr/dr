/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

import java.util.List;

/**
 * Loads source strings for processing.
 * 
 * @since 1.0.0
 */
public interface SourceDao {

   /**
    * 
    * @return a List of source strings
    * @throws Exception
    */
   public List<String> loadSourceStrings() throws Exception;
}