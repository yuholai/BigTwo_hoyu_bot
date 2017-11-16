package bigtwo.game;

public class VoteSkip {
	public static enum VoteSkipRequestResult{
		ACCEPT,
		REJECT_NOT_IN_GAME
	}
	
	public static VoteSkip voteskipAccept
	(Play voter, int nvote, int nfulfill)
	{
		return new VoteSkip(voter, VoteSkipRequestResult.ACCEPT, nvote, nfulfill, false, null, null); 
	}
	
	public static VoteSkip voteskipSkipped
	(Play voter, int nfulfill, Play skipped, PlayerPosition position){
		return new VoteSkip(voter, VoteSkipRequestResult.ACCEPT, nfulfill, nfulfill, true, skipped, position);
	}
	
	public final Play                  voter        ;
	public final VoteSkipRequestResult requestresult;
	public final int                   nvote        ;
	public final int                   nfulfill     ;
	public final boolean               skip         ;
	public final Play                  skipped      ;
	public final PlayerPosition        position     ;
	
	private VoteSkip
	(Play                  voter        ,
	 VoteSkipRequestResult requestresult,
	 int                   nvote        ,
	 int                   nfulfill     ,
	 boolean               skip         ,
	 Play                  skipped      ,
	 PlayerPosition        position     )
	{
		this.voter         = voter        ;
		this.requestresult = requestresult;
		this.nvote         = nvote        ;
		this.nfulfill      = nfulfill     ;
		this.skip          = skip         ;
		this.skipped       = skipped      ;
		this.position      = position     ;
	}
}
