package ua.drovolskyi.cg.lab1.result;

public abstract class PointLocalizationResult {
}


/*
    Types of result of localization point p on planar subdivision:
        - (1) p is on vertex
        - (2) p is on edge
        - (3) p is above or under the graph
        - (4) p is left to leftmost chain
        - (4) p is right to rightmost chain
                (this and previous cases handle situation when only one chain exists)
                (also they handle situation when all chains are similar)
        - (5) Usual case: p is between two chains
*/
