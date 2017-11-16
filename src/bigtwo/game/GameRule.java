package bigtwo.game;

import bigtwo.cardcombination.CardCombinationRule;
import bigtwo.cardcombination.CardCombinationTypeComparator;
import bigtwo.cardcombination.FlushComparator;
import bigtwo.cardcombination.StraightComparator;
import bigtwo.cardcombination.StraightValidator;
import bigtwo.cardcombination.StraightValidatorChain;

public class GameRule {
	private int _ncard;
	private CardCombinationRule _ccrule;
	
	public GameRule(int nCard, CardCombinationRule ccrule){
		this._ncard = nCard;
		this._ccrule = ccrule;
	}
	
	public GameRule(){
		this._ncard = 13;
		this._ccrule = new CardCombinationRule();
	}
	
	public GameRule(GameRule gamerule){
		this(gamerule._ncard, gamerule._ccrule);
	}
	
	// getters
	public int getNCard(){
		return _ncard;
	}
	
	public CardCombinationRule getCardCombinationRule(){
		return _ccrule;
	}
	
	public boolean getAllowTriple(){
		return _ccrule.getAllowTriple();
	}
	
	public StraightValidator getStraightValidator(){
		return _ccrule.getStraightValidator();
	}
	
	public boolean getCanStraightBeatFlush(){
		return _ccrule.getCanStraightBeatFlush();
	}
	
	public StraightComparator getStraightComparator(){
		return _ccrule.getStraightComparator();
	}
	
	public FlushComparator getFlushComparator(){
		return _ccrule.getFlushComparator();
	}
	
	// setters
	public void setNCard(int value){
		this._ncard = value;
	}
	
	public void setCardCombinationRule(CardCombinationRule rule){
		this._ccrule = rule;
	}
	
	public void setAllowTriple(boolean value){
		_ccrule.setAllowTriple(value);
	}
	
	public void setStraightValidator(StraightValidator straightValidator){
		this._ccrule.setStraightValidator(straightValidator);
	}
	
	public void setStraightValidator(int begin, int end){
		this._ccrule.setStraightValidator(
			StraightValidatorChain.getValidator(begin, end)
		);
	}
	
	public void setStraightValidator(int data){
		this._ccrule.setStraightValidator(
			StraightValidatorChain.getValidator(data)
		);
	}
	
	public void setTypeComparator(CardCombinationTypeComparator typeComparator){
		this._ccrule.setTypeComparator(typeComparator);
	}
	
	public void setTypeComparator(boolean canStraightBeatFlush){
		_ccrule.setTypeComparator(canStraightBeatFlush);
	}
	
	public void setStraightComparator(StraightComparator straightComparator){
		_ccrule.setStraightComparator(straightComparator);
	}
	
	public void setStraightComparator(int sc_data){
		_ccrule.setStraightComparator(sc_data);
	}
	
	public void setFlushComparator(FlushComparator flushComparator){
		_ccrule.setFlushComparator(flushComparator);
	}
	
	public void setFlushComparator(int fc_type){
		_ccrule.setFlushComparator(fc_type);
	}
}
