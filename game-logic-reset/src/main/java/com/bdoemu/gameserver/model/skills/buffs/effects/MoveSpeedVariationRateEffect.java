package com.bdoemu.gameserver.model.skills.buffs.effects;

import com.bdoemu.gameserver.model.creature.Creature;
import com.bdoemu.gameserver.model.skills.buffs.ABuffEffect;
import com.bdoemu.gameserver.model.skills.buffs.ActiveBuff;
import com.bdoemu.gameserver.model.skills.buffs.templates.BuffTemplate;
import com.bdoemu.gameserver.model.skills.templates.SkillT;
import com.bdoemu.gameserver.model.stats.elements.BuffElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName MoveSpeedVariationRateEffect
 * @Description TODO
 * @Author JiangBangMing
 * @Date 2019/7/11 20:27
 * VERSION 1.0
 */

public class MoveSpeedVariationRateEffect extends ABuffEffect {

    @Override
    public Collection<ActiveBuff> initEffect(final Creature owner, final Collection<? extends Creature> targets, final SkillT skillT, final BuffTemplate buffTemplate) {
        final List<ActiveBuff> buffs = new ArrayList<ActiveBuff>();
        final Integer[] params = buffTemplate.getParams();
        final int moveSpeedAddPercent = params[0];
        final BuffElement element = new BuffElement(moveSpeedAddPercent);
        for (final Creature target : targets) {
            buffs.add(new ActiveBuff(skillT, buffTemplate, owner, target, element));
        }
        return buffs;
    }

    @Override
    public void applyEffect(final ActiveBuff activeBuff) {
        final Creature target = activeBuff.getTarget();
        target.getGameStats().getMoveSpeedRate().addRateElement(activeBuff.getElement());
        if (target.isPlayer()) {
//            target.sendPacket(new SMSetCharacterSpeedseds(target.getGameStats()));
        }
    }

    @Override
    public void endEffect(final ActiveBuff activeBuff) {
        final Creature target = activeBuff.getTarget();
        target.getGameStats().getMoveSpeedRate().removeRateElement(activeBuff.getElement());
        if (target.isPlayer()) {
//            target.sendPacket(new SMSetCharacterSpeeds(target.getGameStats()));
        }
    }
}