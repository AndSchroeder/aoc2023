package day


object Day16 : Day("16", "46", "51") {
    override fun examplePartOne() = solveOne(getExampleList())
    override fun examplePartTwo() = solveTwo(getExampleList())
    override fun solvePartOne() = solveOne(getInputList())
    override fun solvePartTwo() = solveTwo(getInputList())

    fun solveOne(input: List<String>) = LightGrid.from(input).apply { moveAll() }.visitedFields().toString()

    fun solveTwo(input: List<String>): String {
        val originGrid = LightGrid.from(input)
        val grids = mutableListOf<LightGrid>()
        (0..originGrid.maxY).forEach { y ->
            grids.add(LightGrid.from(input).copy(stack = mutableListOf(LightFieldMove(0, y, LightSource.WEST))))
            grids.add(LightGrid.from(input).copy(stack = mutableListOf(LightFieldMove(originGrid.maxX, y, LightSource.EAST))))
        }
        (0..originGrid.maxX).forEach { x ->
            grids.add(LightGrid.from(input).copy(stack = mutableListOf(LightFieldMove(x, 0, LightSource.NORTH))))
            grids.add(LightGrid.from(input).copy(stack = mutableListOf(LightFieldMove(x, originGrid.maxX, LightSource.SOUTH))))
        }
        return grids.maxOf { grid ->
            grid.moveAll()
            grid.visitedFields()
        }.toString()
    }

    data class LightGrid(val fields: List<List<LightField>>, val stack: MutableList<LightFieldMove> = mutableListOf(LightFieldMove(0,0 , LightSource.WEST))) {

        val maxY = fields.size - 1
        val maxX = fields.first().size - 1

        fun visitedFields() = fields.flatten().count { it.sources.isNotEmpty() }

        fun moveAll() {
            while(stack.isNotEmpty()) {
                move()
            }
        }

        fun move() {
            val move = stack.removeFirst()
            if (!(0..maxX).contains(move.x) || !(0..maxY).contains(move.y)) return
            doMove(move)
        }


        fun doMove(move: LightFieldMove) {
            val field = fields[move.y][move.x]
            if (field.sources.contains(move.source)) return
            field.sources.add(move.source)
            when(move.source) {
                LightSource.NORTH, LightSource.SOUTH -> moveFromVertical(field, move)
                LightSource.EAST, LightSource.WEST -> moveFromHorizontal(field, move)
            }
        }

        private fun moveFromVertical(field: LightField, move: LightFieldMove) {
            val isNorth = move.source == LightSource.NORTH
            val factor = if (isNorth) 1 else - 1
            when (field.type) {
                LightFieldType.HORIZONTAL_SPLITTER -> {
                    stack.add(LightFieldMove(move.x - 1, move.y, LightSource.EAST))
                    stack.add(LightFieldMove(move.x + 1, move.y, LightSource.WEST))
                }
                LightFieldType.NORTH_EAST_MIRROR -> stack.add(LightFieldMove(move.x + factor, move.y, if(isNorth) LightSource.WEST else LightSource.EAST))
                LightFieldType.NORTH_WEST_MIRROR -> stack.add(LightFieldMove(move.x - factor, move.y, if(isNorth) LightSource.EAST else LightSource.WEST))
                else -> stack.add(LightFieldMove(move.x, move.y + factor, move.source))
            }
        }

        private fun moveFromHorizontal(field: LightField, move: LightFieldMove) {
            val isWest = move.source == LightSource.WEST
            val factor = if (isWest) 1 else - 1
            when (field.type) {
                LightFieldType.VERTICAL_SPLITTER -> {
                    stack.add(LightFieldMove(move.x, move.y - 1, LightSource.SOUTH))
                    stack.add(LightFieldMove(move.x, move.y + 1, LightSource.NORTH))
                }
                LightFieldType.NORTH_EAST_MIRROR -> stack.add(LightFieldMove(move.x, move.y + factor, if(isWest) LightSource.NORTH else LightSource.SOUTH))
                LightFieldType.NORTH_WEST_MIRROR -> stack.add(LightFieldMove(move.x, move.y - factor, if(isWest) LightSource.SOUTH else LightSource.NORTH))
                else -> stack.add(LightFieldMove(move.x + factor, move.y, move.source))
            }
        }


        companion object {
            fun from(input: List<String>) = input.mapIndexed { y, line ->
                line.map { field -> LightField(LightFieldType.from(field)) }
            }.let { LightGrid(it) }
        }

        override fun toString() = fields.joinToString("\n") { line -> line.joinToString("") {it.sources.size.toString()} }
    }

    data class LightField(val type: LightFieldType ,val sources: MutableSet<LightSource> = mutableSetOf()) {

        override fun toString() = type.char.toString()
    }

    data class LightFieldMove(val x: Int, val y: Int, val source: LightSource)

    enum class LightFieldType(val char: Char) {
        VERTICAL_SPLITTER('|'),
        HORIZONTAL_SPLITTER('-'),
        NORTH_WEST_MIRROR('/'),
        NORTH_EAST_MIRROR('\\'),
        EMPTY('.');

        companion object {
            fun from(char: Char) = entries.first { char == it.char }
        }
    }

    enum class LightSource{
        NORTH,
        EAST,
        SOUTH,
        WEST,
    }
}
