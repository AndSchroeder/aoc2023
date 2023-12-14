package day


object Day14 : Day("14", "136", "64") {
    override fun examplePartOne() = getExampleList().parseInput().apply { move(Direction.NORTH) }.northScore()
    override fun examplePartTwo() = getExampleList().parseInput().apply { moveCycles() }.northScore()
    override fun solvePartOne() = getInputList().parseInput().apply { move(Direction.NORTH) }.northScore()
    override fun solvePartTwo() = getInputList().parseInput().apply { moveCycles() }.northScore()


    private fun List<String>.parseInput() = map { line ->
        line.map { field -> Rock(RockType.from(field)) }
    }.let { grid -> RockGrid(grid) }


    data class RockGrid(
        var grid: List<List<Rock>>
    ) {
        private val maxX = grid.first().size
        private val maxY = grid.size

        fun move(direction: Direction) {
            do {
                val before = this.toString()
                moveAllTo(direction)
            } while  (before != this.toString())
        }

        fun moveCycles() {
            val memory = mutableListOf<Int>()
            memory.add(this.hashCode())
            do {
                moveCycle()
                memory.add(this.hashCode())
            } while (memory.distinct().size == memory.size)
            val firstIndex = memory.indexOfFirst { this.hashCode() == it }
            val cycle = memory.subList(firstIndex + 1, memory.size)
            val times = (1_000_000_000 - memory.distinct().size) % cycle.size
            repeat(times) { moveCycle() }
        }

        private fun moveCycle() {
            move(Direction.NORTH)
            move(Direction.WEST)
            move(Direction.SOUTH)
            move(Direction.EAST)
        }

        fun northScore() =
            grid.reversed().mapIndexed { y, rocks -> rocks.sumOf { rock -> if (rock.isRounded()) (y + 1) else 0 } }
                .sum().toString()

        private fun moveAllNorth() {
            (0..<maxY).forEach { y ->
                (0..<maxX).forEach { x ->
                    moveNorth(x, y)
                }
            }
        }

        private fun moveAllSouth() {
            (0..<maxY).reversed().forEach { y ->
                (0..<maxX).forEach { x ->
                    moveSouth(x, y)
                }
            }
        }

        private fun moveAllEast() {
            (0..<maxX).forEach { x ->
                (0..<maxY).forEach { y ->
                    moveEast(x, y)
                }
            }
        }

        private fun moveAllWest() {
            (0..<maxX).reversed().forEach { x ->
                (0..<maxY).forEach { y ->
                    moveWest(x, y)
                }
            }
        }

        private fun moveAllTo(direction: Direction) {
            when (direction) {
                Direction.NORTH -> moveAllNorth()
                Direction.SOUTH -> moveAllSouth()
                Direction.EAST -> moveAllEast()
                Direction.WEST -> moveAllWest()
            }

        }

        private fun getRock(x: Int, y: Int) = grid[y][x]

        private fun moveNorth(x: Int, y: Int) {
            val rock = getRock(x, y)
            if (y + 1 < maxY) {
                rock.move(getRock(x, y + 1))
            }
        }

        private fun moveSouth(x: Int, y: Int) {
            val rock = getRock(x, y)
            if (y > 0) {
                rock.move(getRock(x, y - 1))
            }
        }

        private fun moveWest(x: Int, y: Int) {
            val rock = getRock(x, y)
            if (x + 1 < maxX) {
                rock.move(getRock(x + 1, y))
            }
        }

        private fun moveEast(x: Int, y: Int) {
            val rock = getRock(x, y)
            if (x > 0) {
                rock.move(getRock(x - 1, y))
            }
        }

        override fun toString() = grid.joinToString("\n") { line -> line.joinToString("") { field -> "$field" } }
    }

    data class Rock(var type: RockType) {
        fun move(other: Rock) {
            if (other.isRounded() && this.isFree()) {
                this.type = RockType.ROUNDED
                other.type = RockType.EMPTY
            }
        }

        fun isCubeShaped() = type == RockType.CUBE_SHAPED
        fun isRounded() = type == RockType.ROUNDED
        fun isNotBlocked() = type == RockType.ROUNDED
        fun isFree() = type == RockType.EMPTY

        override fun toString() = type.char.toString()

        override fun equals(other: Any?) = if (other is Rock) {
            this.type == other.type
        } else {
            super.equals(other)
        }
    }

    enum class RockType(val char: Char) {
        ROUNDED('O'),
        CUBE_SHAPED('#'),
        EMPTY('.');

        companion object {
            fun from(char: Char) = entries.first { it.char == char }
        }
    }

    enum class Direction() {
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
}
