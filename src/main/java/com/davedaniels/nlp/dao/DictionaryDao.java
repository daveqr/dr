/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

import java.util.List;

/**
 * Loads dictionary of proper nouns.
 * 
 * @since 1.0.0
 */
public interface DictionaryDao {

   public List<String> loadProperNouns();
}