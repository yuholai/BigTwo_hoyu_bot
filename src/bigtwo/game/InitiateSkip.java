package bigtwo.game;

public class InitiateSkip {
	public static enum InitiateSkipResult{
		ACCEPT,
		REJECT_REDUNDANT,
		REJECT_TOO_EARLY
	}
	
	public static InitiateSkip initiateskipAccept
	(Play initiator, Play playertobeskipped, boolean skipped)
	{
		return new InitiateSkip(initiator, playertobeskipped, InitiateSkipResult.ACCEPT, 0, skipped);
	}

	public static InitiateSkip initiateskipTooEarly
	(Play initiator, Play playertobeskipped, int remainsTime)
	{
		return new InitiateSkip(initiator, playertobeskipped, InitiateSkipResult.REJECT_TOO_EARLY, remainsTime, false);
	}
	
	public final Play               initiator        ;
	public final Play               playertobeskipped;
	public final InitiateSkipResult result           ;
	public final int                remainsTime      ;
	public boolean                  skipped          ;
	
	private InitiateSkip
	(Play               initiator        ,
	 Play               playertobeskipped,
	 InitiateSkipResult result           ,
	 int                remainsTime      ,
	 boolean            skipped          )
	{
		this.initiator         = initiator        ;
		this.playertobeskipped = playertobeskipped;
		this.result            = result           ;
		this.remainsTime       = remainsTime      ;
		this.skipped           = skipped          ;
	}
}
