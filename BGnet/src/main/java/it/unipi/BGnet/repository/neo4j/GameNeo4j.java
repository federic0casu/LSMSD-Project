package it.unipi.BGnet.repository.neo4j;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.Record;

public class GameNeo4j {
    private final GraphNeo4j graphNeo4j;
    public GameNeo4j(){ this.graphNeo4j = GraphNeo4j.getIstance();}
    public GraphNeo4j getGraphNeo4j() {
        return graphNeo4j;
    }
    public boolean followGameByGamename(String username, String gamename){
        boolean result = true;
        try {
            graphNeo4j.write("MATCH (u:User) WHERE u.name = $username" +
                            " MATCH (g:Game) WHERE g.name = $gamename" +
                            " MERGE (u)-[:FOLLOWS]->(g)",
                    parameters("username", username, "gamename", gamename));
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean unfollowGameByGamename(String username, String gamename) {
        boolean result = true;
        try {
            graphNeo4j.write("MATCH (u:User {name: $username})-[r:FOLLOWS]->(g:Game {name: $gamename}) DELETE r",
                    parameters("username", username, "gamename", gamename));
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public List<Record> findFollowerNumberByGamename(String gamename) {
        List<Record> result = null;
        try {
            result = graphNeo4j.read("MATCH ()-[:FOLLOWS]->(g:Game)" +
                            " WHERE g.name = $gamename" +
                            " RETURN count(*) as numFollowers",
                    parameters("gamename", gamename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public List<Record> findInCommonFollowers(String username, String gamename){
        try{
            return graphNeo4j.read("MATCH (uA:User)-[:FOLLOWS]->(uTarget:User)" +
                            "-[:FOLLOWS]->(g:Game)" +
                            " WHERE uA.name = $username AND g.name = $gamename" +
                            " RETURN uTarget.name as name, uTarget.imgUrl as imgUrl",
                    parameters("username", username, "gamename", gamename));

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean createNewGame(String gamename){
        boolean result = true;
        try{
            graphNeo4j.write("CREATE (g: Game {name: $gamename})",
                    parameters("gamename", gamename));
        } catch (Exception e){
            e.printStackTrace();
            result=false;
        }
        return result;
    }
    public List<Record> isFollowing(String username, String gamename){
        try{
            return graphNeo4j.read("MATCH (u:User {name: $username}), (g:Game{name: $gamename}) " +
                    "RETURN EXISTS((u)-[:FOLLOWS]->(g)) AS isFollowing",
                    parameters("username", username, "gamename", gamename));

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Record> getFamousGames(){
        try{
            return graphNeo4j.read("MATCH ()-[r:FOLLOWS]->(g:Game) " +
                            "RETURN DISTINCT(g.name) AS game, g.imgUrl AS imgUrl, " +
                            "COUNT(DISTINCT((r))) AS cardinality " +
                            "ORDER BY cardinality DESC " +
                            "LIMIT 4");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Record> getRandomGames(){
        try{
            return graphNeo4j.read("MATCH(g:Game)" +
                    " RETURN g.name AS game, g.imgUrl as imgUrl, rand() as r" +
                    " ORDER BY r" +
                    " LIMIT 4");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean deleteGame(String gamename){
        try{
            graphNeo4j.write("MATCH (g:Game{name:$gamename}) DETACH DELETE g",
            parameters("gamename", gamename));
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Record> getMostPopularGames(){
        try{
            return graphNeo4j.read("MATCH(g:Game)<-[:FOLLOWS]-(u)" +
                    " MATCH (g)<-[:TOURNAMENT_GAME]-(t)" +
                    " WHERE t.isClosed=false" +
                    " RETURN g.name AS gamename, (COUNT(DISTINCT(u))+COUNT(DISTINCT(t))*10) AS popularity" +
                    " ORDER BY popularity DESC" +
                    " LIMIT 5");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
