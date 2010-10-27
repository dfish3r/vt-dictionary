/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

/**
 * Base class for dictionary tests.
 *
 * @author  Middleware Services
 * @version  $Revision: 166 $
 */
public abstract class AbstractDictionaryTest
{

  /** Missing search. */
  public static final String FALSE_SEARCH = "not-found-in-the-dictionary";

  /** Animal names for sorting. */
  public static final String[] ANIMALS;

  /** Case sensitive animal search. */
  public static final String ANIMAL_SEARCH_CS = "Kangaroo";

  /** Case insensitive animal search. */
  public static final String ANIMAL_SEARCH_CI = "kangaroo";

  /** Partial animal search. */
  public static final String ANIMAL_PARTIAL_SEARCH = ".a..us";

  /** Partial animal search results. */
  public static final String[] ANIMAL_PARTIAL_SEARCH_RESULTS_CS =
    new String[] {"Walrus", "Xantus"};

  /** Partial animal search results. */
  public static final String[] ANIMAL_PARTIAL_SEARCH_RESULTS_CI =
    new String[] {"walrus", "xantus"};

  /** Initialization lock. */
  private static final Object LOCK = new Object();

  /** store words from {@link #webFile}. */
  private static Object[][] webWords;

  /** store words from {@link #fbsdFile}. */
  private static Object[][] fbsdWords;


  /**
   * Load animal names.
   */
  static {
    final List<String> animals = new ArrayList<String>();
    animals.add("Aardvark");
    animals.add("Baboon");
    animals.add("Chinchilla");
    animals.add("Donkey");
    animals.add("Emu");
    animals.add("Flamingo");
    animals.add("Gorilla");
    animals.add("Hippopotamus");
    animals.add("Iguana");
    animals.add("Jackal");
    animals.add("Kangaroo");
    animals.add("Lemming");
    animals.add("Marmot");
    animals.add("Narwhal");
    animals.add("Ox");
    animals.add("Platypus");
    animals.add("Quail");
    animals.add("Rhinoceros");
    animals.add("Skunk");
    animals.add("Tortoise");
    animals.add("Uakari ");
    animals.add("Vulture");
    animals.add("Walrus");
    animals.add("Xantus");
    animals.add("Yak");
    animals.add("Zebra");
    Collections.shuffle(animals);
    ANIMALS = animals.toArray(new String[animals.size()]);
  }

  /** Location of the dictionary file. */
  protected String webFile;

  /** Location of the dictionary file. */
  protected String webFileSorted;

  /** Location of the dictionary file. */
  protected String webFileLowerCase;

  /** Location of the dictionary file. */
  protected String webFileLowerCaseSorted;

  /** Location of the dictionary file. */
  protected String fbsdFile;

  /** Location of the dictionary file. */
  protected String fbsdFileSorted;

  /** Location of the dictionary file. */
  protected String fbsdFileLowerCase;

  /** Location of the dictionary file. */
  protected String fbsdFileLowerCaseSorted;


  /**
   * @param  dict1  to load.
   * @param  dict2  to load.
   * @param  dict3  to load.
   * @param  dict4  to load.
   * @param  dict5  to load.
   * @param  dict6  to load.
   * @param  dict7  to load.
   * @param  dict8  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "webFile",
      "webFileSorted",
      "webFileLowerCase",
      "webFileLowerCaseSorted",
      "fbsdFile",
      "fbsdFileSorted",
      "fbsdFileLowerCase",
      "fbsdFileLowerCaseSorted"
    }
  )
  @BeforeClass(groups = {"ttdicttest", "wldicttest"})
  public void createDictionaries(
    final String dict1,
    final String dict2,
    final String dict3,
    final String dict4,
    final String dict5,
    final String dict6,
    final String dict7,
    final String dict8)
    throws Exception
  {
    this.webFile = dict1;
    this.webFileSorted = dict2;
    this.webFileLowerCase = dict3;
    this.webFileLowerCaseSorted = dict4;
    this.fbsdFile = dict5;
    this.fbsdFileSorted = dict6;
    this.fbsdFileLowerCase = dict7;
    this.fbsdFileLowerCaseSorted = dict8;

    synchronized (LOCK) {
      if (webWords == null) {
        webWords = this.createWords(webFileSorted);
      }
      if (fbsdWords == null) {
        fbsdWords = this.createWords(fbsdFileSorted);
      }
    }
  }


  /** @throws  Exception  On test failure. */
  @AfterSuite(groups = {"ttdicttest", "wldicttest"})
  public void tearDown()
    throws Exception
  {
    webWords = null;
    fbsdWords = null;
  }


  /**
   * Returns an array of words from the supplied file.
   *
   * @param  dictFile  <code>String</code> to read
   *
   * @return  <code>Object[][]</code> containing words
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  private Object[][] createWords(final String dictFile)
    throws IOException
  {
    final FileWordList fwl = new FileWordList(
      new RandomAccessFile(dictFile, "r"));
    final Object[][] allWords = new Object[fwl.size()][1];
    for (int i = 0; i < fwl.size(); i++) {
      allWords[i] = new Object[] {fwl.get(i), };
    }
    fwl.close();
    return allWords;
  }


  /**
   * Sample word data.
   *
   * @return  word data
   *
   * @throws  IOException  if an error occurs reading {@link #webFile}
   */
  @DataProvider(name = "all-web-words")
  public Object[][] createAllWebWords()
    throws IOException
  {
    return webWords;
  }


  /**
   * Sample word data.
   *
   * @return  word data
   *
   * @throws  IOException  if an error occurs reading {@link #fbsdFile}
   */
  @DataProvider(name = "all-fbsd-words")
  public Object[][] createAllFbsdWords()
    throws IOException
  {
    return fbsdWords;
  }
}
