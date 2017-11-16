package bigtwo.game;

import java.util.ArrayList;

import bigtwo.game.Game.GameResult;

public class GameHistory{
	public static class RecordEntry{
		public static final int
			PLAY_CARD = 0,
			PASS      = 1,
			LEAVE     = 2,
			RESULT    = 3,
			SKIPPED   = 4;
		
		public final int type;
		public final Play play;
		public final PlayCard playcard;
		public final int nhandcard;
		public final GameResult result;
		
		public RecordEntry
		(int type, Play play, PlayCard playcard, 
			int nhandcard, GameResult result){
			this.type = type;
			this.play = play;
			this.playcard = playcard;
			this.nhandcard = nhandcard;
			this.result = result;
		}
	}
	
	protected ArrayList<RecordEntry> records;
	
	public GameHistory() {
		records = new ArrayList<>();
	}
	
	public void recordPlayCard(PlayCard playcard) {
		records.add(
			new RecordEntry(
				RecordEntry.PLAY_CARD,
				null,
				playcard,
				playcard.sender.getHand().getNumberOfHandcards(),
				null
		));
		
	}

	public void recordPass(Play play) {
		records.add(
			new RecordEntry(
				RecordEntry.PASS,
				play,
				null,
				0,
				null)
		);
	}

	public void recordLeave(Play play) {
		records.add(
			new RecordEntry
			(
				RecordEntry.LEAVE, 
				play, 
				null, 
				0, 
				null
			)
		);
	}

	public void recordGameResult(GameResult result) {
		records.add(
			new RecordEntry(
				RecordEntry.RESULT, 
				null, 
				null, 
				0, 
				result));		
	}

	public void recordSkipped(Play play) {
		records.add(
			new RecordEntry(
				RecordEntry.SKIPPED, 
				play, 
				null, 
				0, 
				null));		
	}
	
	public ArrayList<RecordEntry> getEntries(){
		return records;
	}
}
