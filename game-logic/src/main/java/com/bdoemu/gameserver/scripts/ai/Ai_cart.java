package com.bdoemu.gameserver.scripts.ai;

import com.bdoemu.commons.thread.ThreadPool;
import com.bdoemu.commons.utils.Rnd;
import com.bdoemu.gameserver.model.ai.deprecated.*;
import com.bdoemu.gameserver.model.creature.Creature;
import com.bdoemu.gameserver.model.actions.enums.*;
import com.bdoemu.gameserver.model.chat.enums.EChatNoticeType;
import com.bdoemu.gameserver.model.misc.enums.ETradeCommerceType;
import com.bdoemu.gameserver.model.weather.enums.EWeatherFactorType;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author H1X4
 */

@SuppressWarnings("all")
@IAIName("cart")
public class Ai_cart extends CreatureAI {
	public Ai_cart(Creature actor, Map<Long, Integer> aiVariables) {
		super(actor, aiVariables);
	}

	protected void InitialState(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xAE5FEAC2L /*InitialState*/);
		setVariable(0x95AC12F8L /*_triggerkey*/, getVariable(0x60D03638L /*AI_triggerkey*/));
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Start_Wait(blendTime), 10000));
	}

	protected void TerminateState(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.none);
		setState(0xF74207F6L /*TerminateState*/);
		// Prevent recursion;
	}

	protected void Start_Wait(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x9D7E92CDL /*Start_Wait*/);
		if (!isRiderExist() && checkTrigger(getVariable(0x95AC12F8L /*_triggerkey*/))) {
			if (changeState(state -> Suicide_Die_Wait(blendTime)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Start_Wait(blendTime), 1000));
	}

	protected void Wait(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x866C7489L /*Wait*/);
		clearAggro(true);
		if(getCallCount() == 25) {
			if (changeState(state -> Die(blendTime)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait(blendTime), 1000));
	}

	protected void Wait_1(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x96AB8779L /*Wait_1*/);
		clearAggro(true);
		if(getCallCount() == 10) {
			if (changeState(state -> Wait(blendTime)))
				return;
		}
		doAction(1569894142L /*WAIT_1*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_1(blendTime), 1000));
	}

	protected void Idle(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xD43ADE20L /*Idle*/);
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Idle(blendTime), 10000));
	}

	protected void Suicide_Die_Wait(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x64526570L /*Suicide_Die_Wait*/);
		if (getActor().isVehicle() && getActor().getOwner() != null) {
			if (changeState(state -> Suicide_Die(0.3)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Die(blendTime), 3000));
	}

	protected void Suicide_Die(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.dead);
		setState(0x2BD8C797L /*Suicide_Die*/);
		doAction(2544805566L /*DIE*/, blendTime, onDoActionEnd -> scheduleState(state -> Suicide_Die(blendTime), 1000));
	}

	protected void Die(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.dead);
		setState(0x90DBFD38L /*Die*/);
		clearAggro(true);
		doAction(2544805566L /*DIE*/, blendTime, onDoActionEnd -> scheduleState(state -> Die(blendTime), 1000));
	}

	protected void UnderWaterDead(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.dead);
		setState(0xB63D3C22L /*UnderWaterDead*/);
		clearAggro(true);
		doAction(2544805566L /*DIE*/, blendTime, onDoActionEnd -> scheduleState(state -> UnderWaterDead(blendTime), 1000));
	}

	@Override
	public EAiHandlerResult HandleTakeDamage(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleUnderWaterDead(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (changeState(state -> UnderWaterDead(0.1)))
			return EAiHandlerResult.CHANGE_STATE;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleOnResetAI(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (getState() != 0x90DBFD38L /*Die*/) {
			if (changeState(state -> TerminateState(0.1)))
				return EAiHandlerResult.CHANGE_STATE;
		}
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleRideOn(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleRideEnd(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (getState() != 0x90DBFD38L /*Die*/) {
			if (changeState(state -> Wait_1(0.1)))
				return EAiHandlerResult.CHANGE_STATE;
		}
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleDead(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (changeState(state -> Die(0.1)))
			return EAiHandlerResult.CHANGE_STATE;
		return EAiHandlerResult.BYPASS;
	}
}