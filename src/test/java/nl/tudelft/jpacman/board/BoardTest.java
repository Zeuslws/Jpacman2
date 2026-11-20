package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Tests valid boards and their assertion contracts with assertions enabled. */
class BoardTest {
    /** A valid one-cell board returns its square. */
    @Test
    void validSquareAt() {
        Square square = new BasicSquare();
        Board board = new Board(new Square[][] {{square}});
        assertThat(board.squareAt(0, 0)).isSameAs(square);
    }

    /** A null cell is rejected before squareAt can be called. */
    @Test
    void nullSquareAt() {
        assertThatThrownBy(() -> {
            Board board = new Board(new Square[][] {{null}});
            board.squareAt(0, 0);
        }).isInstanceOf(AssertionError.class)
            .hasMessage("Initial grid cannot contain null squares");
    }

    /** squareAt also checks its postcondition if the backing grid changes. */
    @Test
    void squareAtRejectsNullAfterConstruction() {
        Square[][] grid = {{new BasicSquare()}};
        Board board = new Board(grid);
        grid[0][0] = null;
        assertThatThrownBy(() -> board.squareAt(0, 0))
            .isInstanceOf(AssertionError.class)
            .hasMessage("Follows from invariant.");
    }
}
