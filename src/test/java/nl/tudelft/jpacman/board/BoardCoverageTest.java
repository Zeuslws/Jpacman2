package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Boundary and invariant tests complementing the supplied BoardTest.
 */
class BoardCoverageTest {

    private final Square[][] grid = {
        {mock(Square.class), mock(Square.class), mock(Square.class)},
        {mock(Square.class), mock(Square.class), mock(Square.class)}
    };
    private final Board board = new Board(grid);

    /**
     * Contract checks in Board require JVM assertions, enabled by Gradle tests.
     */
    @BeforeAll
    static void requireAssertions() {
        assertTrue(Board.class.desiredAssertionStatus(),
            "Run tests with JVM assertions enabled (-ea).");
    }

    /**
     * Covers valid corners and each short-circuit rejection at the boundary.
     * @param x column coordinate
     * @param y row coordinate
     * @param expected whether the coordinate belongs to the board
     */
    @ParameterizedTest
    @CsvSource({
        "0, 0, true", "1, 0, true", "0, 2, true", "1, 2, true",
        "-1, 0, false", "2, 0, false", "0, -1, false", "0, 3, false"
    })
    void checksEachBoundary(int x, int y, boolean expected) {
        assertEquals(expected, board.withinBorders(x, y));
    }

    /**
     * A grid containing only squares satisfies the invariant.
     */
    @Test
    void acceptsGridWithoutNullSquares() {
        assertTrue(board.invariant());
    }

    /**
     * The stored array is shared; a later null cell must be detected.
     */
    @Test
    void detectsNullSquareAfterGridMutation() {
        grid[1][2] = null;
        assertFalse(board.invariant());
    }

    /**
     * A null grid violates the constructor precondition.
     */
    @Test
    void rejectsNullGrid() {
        assertThrows(AssertionError.class, () -> new Board(null));
    }

    /**
     * A null cell violates the constructor's initial invariant.
     */
    @Test
    void rejectsGridContainingNullSquare() {
        grid[1][2] = null;
        assertThrows(AssertionError.class, () -> new Board(grid));
    }

    /**
     * Access outside any of the four edges violates squareAt's precondition.
     * @param x column coordinate
     * @param y row coordinate
     */
    @ParameterizedTest
    @CsvSource({"-1, 0", "2, 0", "0, -1", "0, 3"})
    void rejectsSquareAccessOutsideBoard(int x, int y) {
        assertThrows(AssertionError.class, () -> board.squareAt(x, y));
    }
}
