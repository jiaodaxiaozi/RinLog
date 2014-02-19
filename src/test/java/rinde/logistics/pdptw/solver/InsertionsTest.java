/**
 * 
 */
package rinde.logistics.pdptw.solver;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * @author Rinde van Lon <rinde.vanlon@cs.kuleuven.be>
 * 
 */
public class InsertionsTest {

  static final String A = "A";
  static final String B = "B";
  static final String C = "C";
  static final String Z = "Z";

  /**
   * Tests whether all insertions are created.
   */
  @Test
  public void testPlusOneInsertions() {
    List<ImmutableList<String>> strings = Insertions.plusOneInsertions(
        ImmutableList.<String> of(), Z);
    assertEquals(1, strings.size());
    assertEquals(asList(Z), strings.get(0));

    strings = Insertions.plusOneInsertions(ImmutableList.of(A), Z);
    assertEquals(2, strings.size());
    assertEquals(asList(Z, A), strings.get(0));
    assertEquals(asList(A, Z), strings.get(1));

    strings = Insertions.plusOneInsertions(ImmutableList.of(A, B), Z);
    assertEquals(3, strings.size());
    assertEquals(asList(Z, A, B), strings.get(0));
    assertEquals(asList(A, Z, B), strings.get(1));
    assertEquals(asList(A, B, Z), strings.get(2));

    strings = Insertions.plusOneInsertions(ImmutableList.of(A, B, C), Z);
    assertEquals(4, strings.size());
    assertEquals(asList(Z, A, B, C), strings.get(0));
    assertEquals(asList(A, Z, B, C), strings.get(1));
    assertEquals(asList(A, B, Z, C), strings.get(2));
    assertEquals(asList(A, B, C, Z), strings.get(3));
  }

  /**
   * Tests whether all two insertions are created.
   */
  @Test
  public void testPlusTwoInsertions() {
    List<ImmutableList<String>> strings = Insertions.plusTwoInsertions(
        ImmutableList.of(A, B, C), Z, 0);
    assertEquals(10, strings.size());
    assertEquals(asList(Z, Z, A, B, C), strings.get(0));
    assertEquals(asList(Z, A, Z, B, C), strings.get(1));
    assertEquals(asList(Z, A, B, Z, C), strings.get(2));
    assertEquals(asList(Z, A, B, C, Z), strings.get(3));

    assertEquals(asList(A, Z, Z, B, C), strings.get(4));
    assertEquals(asList(A, Z, B, Z, C), strings.get(5));
    assertEquals(asList(A, Z, B, C, Z), strings.get(6));

    assertEquals(asList(A, B, Z, Z, C), strings.get(7));
    assertEquals(asList(A, B, Z, C, Z), strings.get(8));

    assertEquals(asList(A, B, C, Z, Z), strings.get(9));

    assertEquals(strings.size(), newHashSet(strings).size());

    // test empty
    strings = Insertions.plusTwoInsertions(ImmutableList.<String> of(), Z, 0);
    assertEquals(1, strings.size());
    assertEquals(asList(Z, Z), strings.get(0));
  }

  /**
   * Tests illegal negative start index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlusOneInsertionsFail1() {
    Insertions.plusOneInsertions(ImmutableList.of(A, B), Z, -1);
  }

  /**
   * Tests illegal start index higher than list size.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlusOneInsertionsFail2() {
    Insertions.plusOneInsertions(ImmutableList.of(A, B), Z, 3);
  }
}
