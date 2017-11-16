package bigtwo.game;

public class Leave {
	public static enum LeaveResult{
		ACCEPT,
		REJECT_NOT_IN_GAME,
		FAILED_NO_GAME_IN_GROUP
	}
	
	static Leave newLeaveWhileForming(Play play, int nplay, int maxnplay){
		return new Leave(
			LeaveResult.ACCEPT, 
			play, 
			false, 
			nplay, 
			maxnplay, 
			false, 
			null, 
			null, 
			null,
			null, 
			null);	
	}
	
	static Leave newLeaveEldestHand(Play eldesthand, Play nextPlay){
		return new Leave(
			LeaveResult.ACCEPT, 
			eldesthand, 
			true, 
			0, 
			0, 
			false, 
			null, 
			PlayerPosition.ELDEST_HAND, 
			nextPlay,
			null, 
			null);
	}

	static Leave newLeaveNewLeader(Play newleader, Play nextPlay){
		return new Leave(
			LeaveResult.ACCEPT, 
			newleader, 
			true, 
			0, 
			0, 
			false, 
			null, 
			PlayerPosition.NEW_LEADER, 
			nextPlay,
			null, 
			null);		
	}
	
	static Leave newLeaveWhilePlayingOnTurn(Play play, Play nextPlay){
		return new Leave(
			LeaveResult.ACCEPT, 
			play, 
			true, 
			0, 
			0, 
			false, 
			null, 
			PlayerPosition.PLAYER, 
			nextPlay,
			null, 
			null);	
	}

	static Leave newLeaveWhilePlayingNotOnTurn(Play play){
		return new Leave(
			LeaveResult.ACCEPT, 
			play, 
			true, 
			0, 
			0, 
			false, 
			null, 
			null, 
			null,
			null, 
			null);	
	}
	
	static Leave newLeaveTerminateNotEnoughPlayer(Play play, Play remains){
		return new Leave(
			LeaveResult.ACCEPT,
			play,
			true,
			0,
			0,
			true,
			remains,
			null,
			null,
			null,
			null);
	}
	
	static Leave newLeaveRejectNotInGame(){
		return new Leave(
			LeaveResult.REJECT_NOT_IN_GAME,
			null,
			false,
			0,
			0,
			false,
			null,
			null,
			null,
			null,
			null
			);
	}
	
	static Leave newLeaveFailedNoGameInGroup(Player player, Group group){
		return new Leave(
			LeaveResult.FAILED_NO_GAME_IN_GROUP,
			null,
			false, 
			0, 
			0, 
			false,
			null, 
			null, 
			null, 
			player, 
			group);
	}
	
	public final LeaveResult    result        ;
	public final Play           play          ;
	public final boolean        isGameStarted ;
	public final int            nplay         ;
	public final int            maxnplay      ;
	public final boolean        isCancel      ;
	public final Play           remains       ;
	public final PlayerPosition position      ;
	public final Play           nextPlay      ;
	public final Player         player        ;
	public final Group          group         ;
	
	private Leave
	(LeaveResult    result,
	 Play           play,     
	 boolean        isGameStarted,         
	 int            nplay,
	 int            maxnplay,
	 boolean        isCancel,
	 Play           remains,
	 PlayerPosition position,     
	 Play           nextPlay,
	 Player         player,
	 Group          group)
	{
		this.result        = result       ;
		this.play          = play         ;
		this.isGameStarted = isGameStarted;
		this.nplay         = nplay        ;
		this.maxnplay      = maxnplay     ;
		this.isCancel      = isCancel     ;
		this.remains       = remains      ;
		this.position      = position     ;
		this.nextPlay      = nextPlay     ;
		this.player        = player       ;
		this.group         = group        ;
	}
}
