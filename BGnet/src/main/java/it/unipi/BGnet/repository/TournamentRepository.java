package it.unipi.BGnet.repository;

import it.unipi.BGnet.model.Tournament;
import it.unipi.BGnet.repository.neo4j.TournamentNeo4j;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TournamentRepository {
    private TournamentNeo4j tournamentNeo4j = new TournamentNeo4j();
    public boolean createTournament(int maxPlayers, String date, String modalities, String playersPerMatch, String duration, String gamename, String creator){
        return tournamentNeo4j.createTournament(maxPlayers, date, modalities, playersPerMatch, duration, gamename, creator);
    }
    public boolean closeTournament(int tournamentId){
        return tournamentNeo4j.closeTournament(tournamentId);
    }
    public boolean addTournamentPartecipant(String username, int tournamentid){
        return tournamentNeo4j.addTournamentPartecipant(username, tournamentid);
    }
    public boolean removeTournamentPartecipant(String username, int tournamentId){
        return tournamentNeo4j.removeTournamentPartecipant(username, tournamentId);
    }
    public List<Tournament> getTournamentsByGamename(String gamename){
        List<Tournament> tournamentList= new ArrayList<>();
        for(Record r: tournamentNeo4j.getTournamentsByGamename(gamename)){
            int tid = r.get("id").asInt();
            String date = r.get("date").asString();
            String duration = r.get("duration").asString();
            int maxPlayers = r.get("maxPlayers").asInt();
            String modalities = r.get("modalities").asString();
            String playersPerMatch = r.get("playersPerMatch").asString();
            String creator = r.get("creator").asString();
            List<String> participants = r.get("participants").asList(Value::asString);
            Boolean isClosed = r.get("isClosed").isNull() ? false : r.get("isClosed").asBoolean();
            Tournament t = new Tournament(tid, date, duration, maxPlayers, modalities, playersPerMatch, participants, gamename, creator, isClosed);
            tournamentList.add(t);
        }
        return tournamentList;
    }
    public List<String> getPartecipantsByTournamentId(int tournamentId){
        List<String> partecipantsList = new ArrayList<>();
        for(Record r: tournamentNeo4j.getPartecipantsByTournamentId(tournamentId)){
            partecipantsList.add(r.get("username").asString());
        }
        return partecipantsList;
    }
    public List<Tournament> getInCommonTournaments(String userA, String userB){
        List<Tournament> tournamentList= new ArrayList<>();
        List<Record> tournamentRecordList = tournamentNeo4j.getInCommonTournaments(userA, userB);
        if(tournamentRecordList!=null) {
            for (Record r : tournamentNeo4j.getInCommonTournaments(userA, userB)) {
                int tid = r.get("id").asInt();
                String date = r.get("date").asString();
                String duration = r.get("duration").asString();
                int maxPlayers = r.get("maxPlayers").asInt();
                String modalities = r.get("modalities").asString();
                String playersPerMatch = r.get("playersPerMatch").asString();
                Boolean isClosed = r.get("isClosed").asBoolean();
                String gameName = r.get("gameName").asString();
                Tournament t = new Tournament(tid, date, duration, maxPlayers, modalities, playersPerMatch, null, gameName, null, isClosed);
                tournamentList.add(t);
            }
        }
        return tournamentList;
    }
    public String getGameByTournamentId(int tournamentId){
        if(tournamentNeo4j.getGameByTournamentId(tournamentId)!=null){
            return tournamentNeo4j.getGameByTournamentId(tournamentId).get(0).get("gamename").asString();
        }
        return null;
    }
    public String getCreatorByTournamentId(int tournamentId){
        if(tournamentNeo4j.getCreatorByTournamentId(tournamentId)!=null){
            return tournamentNeo4j.getCreatorByTournamentId(tournamentId).get(0).get("username").asString();
        }
        return null;
    }
    public List<Tournament> getTournamentsByUser(String username){
        List<Tournament> tournamentList= new ArrayList<>();
        for(Record r: tournamentNeo4j.getTournamentsByUser(username)){
            int tid = r.get("id").asInt();
            String date = r.get("date").asString();
            String duration = r.get("duration").asString();
            int maxPlayers = r.get("maxPlayers").asInt();
            String modalities = r.get("modalities").asString();
            String playersPerMatch = r.get("playersPerMatch").asString();
            boolean isClosed = r.get("isClosed").asBoolean();
            String tournamentGame = r.get("gameName").asString();
            Tournament t = new Tournament(tid, date, duration, maxPlayers, modalities, playersPerMatch, null, tournamentGame, null, isClosed);
            tournamentList.add(t);
        }
        return tournamentList;
    }
    public boolean isParticipating(String username, int tournamentId){
        return tournamentNeo4j.isParticipating(username, tournamentId).get(0).get("isParticipating").asBoolean();
    }
    public boolean isCreator(String username, int tournamentId){
        return tournamentNeo4j.isCreator(username, tournamentId).get(0).get("isParticipating").asBoolean();
    }
}
