package com.machinespray.ROYAL.items.rings;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.Values;
import com.machinespray.ROYAL.render.RenderGUIEvent;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.machinespray.ROYAL.Values.toOutline;

public enum RingAction implements Constants {
    AGGRAVATE_MONSTER, CONFLICT, LEVITATION, SEE_INVISIBLE, TELEPORTATION, HUNGER, PROTECTION, REGENERATION, FIRE_RESISTANCE, STRENGTH, PARANOIA;
    public int id;

    private void discover(Entity player) {
        IKnowledgeHandler knowledge = Main.getHandler(player);
        if (!knowledge.hasKnowledge(getKnowledgeName())) {
            if (!player.world.isRemote)
                RenderGUIEvent.buffer.add(
                        "You discover this is a ring of\n" + getKnowledgeName() + "!");
            knowledge.addKnowledge(getKnowledgeName());
        }
    }

    public String getKnowledgeName() {
        return this.name().replace("_", " ").toLowerCase();
    }

    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        String buc = itemstack.getTagCompound().getString(BUC);
        if (this.equals(AGGRAVATE_MONSTER)) {
            AxisAlignedBB box = new AxisAlignedBB(player.posX - 5,
                    player.posY - 5, player.posZ - 5, player.posX + 5,
                    player.posY + 5, player.posZ + 5);
            if (buc.equals(CURSED))
                box = box.expandXyz(2);
            if (buc.equals(BLESSED))
                box = box.expandXyz(.5);
            List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                    EntityLiving.class, box);
            for (EntityLiving e : list) {
                if (e.getAttackTarget() == null
                        || e.getAttackTarget() != player) {
                    e.setAttackTarget(player);
                }
            }
        } else if (this.equals(CONFLICT)) {
            AxisAlignedBB box = new AxisAlignedBB(player.posX - 5,
                    player.posY - 5, player.posZ - 5, player.posX + 5,
                    player.posY + 5, player.posZ + 5);
            if (buc.equals(CURSED))
                box = box.expandXyz(.5);
            if (buc.equals(BLESSED))
                box = box.expandXyz(2);
            List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                    EntityLiving.class, box);
            for (EntityLiving e : list) {
                if (e.getAttackTarget() == null) {
                    EntityLiving entity = list.get(Values.random.nextInt(list
                            .size()));
                    if (!entity.equals(e))
                        e.setAttackTarget(entity);
                }

            }
            //TODO optimize potions and discoveries(Doesn't matter too much, but is a thing)
        } else if (this.equals(LEVITATION)) {
            player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:levitation")
                    , 20, -1, false, false));
            player.addPotionEffect(new PotionEffect(
                    Potion.getPotionFromResourceLocation("minecraft:speed"), 20, 255, false, false));
            discover(player);

        } else if (this.equals(SEE_INVISIBLE)) {
            List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                    EntityLiving.class, new AxisAlignedBB(player.posX - 5,
                            player.posY - 5, player.posZ - 5,
                            player.posX + 5, player.posY + 5,
                            player.posZ + 5));
            for (EntityLiving e : list) {
                if (Main.proxy.isRemote())
                    if (e.getActivePotionEffect(Potion.getPotionById(14)) != null)
                        toOutline.add(e);
            }
        } else if (this.equals(TELEPORTATION)) {
            if (Values.random.nextInt(200) > 198)
                if (!player.world.isRemote) {
                    double d0 = player.posX;
                    double d1 = player.posY;
                    double d2 = player.posZ;

                    for (int i = 0; i < 16; i++) {
                        double d3 = player.posX
                                + (player.getRNG().nextDouble() - 0.5D)
                                * 16.0D;
                        double d4 = MathHelper
                                .clamp(player.posY
                                                + (double) (player.getRNG()
                                                .nextInt(16) - 8), 0.0D,
                                        (double) (player.world
                                                .getActualHeight() - 1));
                        double d5 = player.posZ
                                + (player.getRNG().nextDouble() - 0.5D)
                                * 16.0D;

                        if (player.isRiding()) {
                            player.dismountRidingEntity();
                        }

                        if (player.attemptTeleport(d3, d4, d5)) {
                            player.world.playSound((EntityPlayer) null, d0,
                                    d1, d2,
                                    SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                    SoundCategory.PLAYERS, 1.0F, 1.0F);
                            player.playSound(
                                    SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                    1.0F, 1.0F);
                            IKnowledgeHandler knowledge = Main
                                    .getHandler(player);
                        }
                        break;

                    }
                }
        } else if (this.equals(HUNGER)) {
            player.addPotionEffect(new PotionEffect(Potion
                    .getPotionById(17), 21, 0, false, false));
            IKnowledgeHandler knowledge = Main.getHandler(player);
            if (!knowledge.hasKnowledge("hunger")) {
                if (!player.world.isRemote)
                    discover(player);
                knowledge.addKnowledge("hunger");
            }
        } else if (this.equals(PROTECTION)) {
            player.addPotionEffect(new PotionEffect(Potion
                    .getPotionById(11), 21, 0, false, false));
            IKnowledgeHandler knowledge = Main.getHandler(player);
            if (!knowledge.hasKnowledge("protection")) {
                if (!player.world.isRemote)
                    discover(player);
                knowledge.addKnowledge("protection");
            }
        } else if (this.equals(REGENERATION)) {
            if (player.getActivePotionEffect(Potion.getPotionById(10)) == null)
                player.addPotionEffect(new PotionEffect(Potion
                        .getPotionById(10), 80, 0, false, false));
            IKnowledgeHandler knowledge = Main.getHandler(player);
            if (!knowledge.hasKnowledge("regeneration")) {
                if (!player.world.isRemote)
                    discover(player);
                knowledge.addKnowledge("regeneration");
            }
        } else if (this.equals(FIRE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(Potion
                    .getPotionById(12), 10, 0, false, false));
            IKnowledgeHandler knowledge = Main.getHandler(player);
            if (!knowledge.hasKnowledge("fire resistance")) {
                if (!player.world.isRemote)
                    discover(player);
                knowledge.addKnowledge("fire resistance");
            }
        } else if (this.equals(STRENGTH)) {
            player.addPotionEffect(new PotionEffect(
                    Potion.getPotionById(5), 10, 0, false, false));
            IKnowledgeHandler knowledge = Main.getHandler(player);
            if (!knowledge.hasKnowledge("strength")) {
                if (!player.world.isRemote)
                    discover(player);
                knowledge.addKnowledge("strength");
            }
        } else if (this.equals(PARANOIA)) {
            if (Values.random.nextInt(200) > 198)
                if (!player.world.isRemote) {
                    double d0 = player.posX;
                    double d1 = player.posY;
                    double d2 = player.posZ;
                    int i = Values.random.nextInt(SoundEvents.class
                            .getDeclaredFields().length);
                    try {
                        Object j = null;
                        j = SoundEvents.class.getDeclaredFields()[i].get(j);
                        player.world.playSound(null, d0, d1,
                                d2, (SoundEvent) j, SoundCategory.PLAYERS,
                                1.0F, 1.0F);
                        player.playSound((SoundEvent) j, 1.0F, 1.0F);
                    } catch (IllegalArgumentException e) {
                        player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED,
                                1.0F, 1.0F);
                    } catch (IllegalAccessException e) {
                        player.playSound(SoundEvents.ENTITY_ZOMBIE_AMBIENT,
                                1.0F, 1.0F);
                    } catch (SecurityException e) {
                        player.playSound(SoundEvents.ENTITY_PLAYER_HURT,
                                1.0F, 1.0F);
                    }

                }
        }
    }
}