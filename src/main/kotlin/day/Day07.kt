package day


object Day07 : Day("07", "6440", "5905") {
    override fun examplePartOne() = getExampleList().parseInputOne().getScore()
    override fun examplePartTwo() = getExampleList().parseInputTwo().getScore()
    override fun solvePartOne() = getInputList().parseInputOne().getScore()
    override fun solvePartTwo() = getInputList().parseInputTwo().getScore()

    private fun List<CamelCardHand>.getScore() =
        this.sorted().mapIndexed { index, camelCardHand -> (index + 1) * camelCardHand.bid }.sum().toString()

    private fun List<String>.parseInputOne() = map { line -> parseHand(line, false) }
    private fun List<String>.parseInputTwo() = map { line -> parseHand(line, true) }

    private fun parseHand(line: String, useJokers: Boolean): CamelCardHand {
        val (handString, bidString) = line.split(" ")
        val cards = handString.map { cardString -> CamelCard(CamelCardType.from(cardString.toString(), useJokers)) }
        return CamelCardHand(cards, bidString.toInt(), useJokers, CamelCardHand.determineType(useJokers, cards))
    }

    data class CamelCardHand(
        val cards: List<CamelCard>,
        val bid: Int,
        val useJokers: Boolean,
        val type: CamelCardHandType,
    ) : Comparable<CamelCardHand> {

        private fun compareHighCards(other: CamelCardHand): Int {
            this.cards.zip(other.cards).forEach { (cardOne, cardTwo) ->
                if (cardOne > cardTwo) {
                    return 1
                } else if (cardOne < cardTwo) {
                    return -1
                }
            }
            return 0
        }

        companion object {
            fun determineType(useJokers: Boolean, cards: List<CamelCard>): CamelCardHandType {
                val jokers = if (useJokers) cards.filter { it.type == CamelCardType.JOKER }.size else 0
                var sorted =
                    cards.filter { it.type != CamelCardType.JOKER }
                        .groupingBy { it }
                        .eachCount()
                        .map { (_, value) -> value }
                        .sorted()
                        .reversed()
                        .toMutableList()

                if (sorted.size == 0) sorted = mutableListOf(0)

                sorted[0] = sorted.first() + jokers

                return when {
                    (sorted.first()) == 5 -> CamelCardHandType.FIVE_OF_A_KIND
                    (sorted.first()) == 4 -> CamelCardHandType.FOUR_OF_A_KIND
                    (sorted.first()) == 3 && sorted.last() == 2 -> CamelCardHandType.FULL_HOUSE
                    (sorted.first()) == 3 -> CamelCardHandType.THREE_OF_A_KIND
                    (sorted.first()) == 2 && sorted[1] == 2 -> CamelCardHandType.TWO_PAIR
                    (sorted.first()) == 2 -> CamelCardHandType.ONE_PAIR
                    else -> CamelCardHandType.HIGH_CARD
                }
            }
        }

        override fun compareTo(other: CamelCardHand) = when {
            this.type.rank > other.type.rank -> 1
            this.type.rank < other.type.rank -> -1
            this.type == other.type && this.compareHighCards(other) > 0 -> 1
            this.type == other.type && this.compareHighCards(other) < 0 -> -1
            else -> 0
        }
    }

    data class CamelCard(
        val type: CamelCardType
    ) : Comparable<CamelCard> {
        override fun compareTo(other: CamelCard) = this.type.rank - other.type.rank
    }

    enum class CamelCardType(val shortName: String, val rank: Int) {
        ACE("A", 14),
        KING("K", 13),
        QUEEN("Q", 12),
        JACK("J", 11),
        TEN("T", 10),
        NINE("9", 9),
        EIGHT("8", 8),
        SEVEN("7", 7),
        SIX("6", 6),
        FIVE("5", 5),
        FOUR("4", 4),
        THREE("3", 3),
        TWO("2", 2),
        JOKER("J", 1);

        companion object {
            fun from(shortName: String, useJokers: Boolean) =
                entries.filter { entry -> entry.shortName == shortName }
                    .let { if (useJokers) it.last() else it.first() }
        }
    }

    enum class CamelCardHandType(val rank: Int) {
        FIVE_OF_A_KIND(6),
        FOUR_OF_A_KIND(5),
        FULL_HOUSE(4),
        THREE_OF_A_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0),
    }
}
