package com.rikmuld.camping.inventory

import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.corerm.features.tabbed.SlotWithTabs
import com.rikmuld.corerm.inventory.slots.{SlotChangingInventory, SlotDisable, SlotNot, SlotOnly}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot._
import net.minecraft.inventory.{IInventory, InventoryCrafting, Slot, SlotCrafting}
import net.minecraft.item.{Item, ItemArmor, ItemStack}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class SlotState(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) with SlotDisable {
  var stateX: Int = xFlag
  var stateY: Int = yFlag

  override def enable() {
    xPos = stateX
    yPos = stateY
  }

  def setStateX(state: Int) =
    stateX = xFlag - (18 * state)

  def setStateY(state: Int) =
    stateY = yFlag - (18 * state)
}

class SlotCooking(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) {
  var active: Boolean =
    false

  var equipment: CookingEquipment =
    _

  var fire: TileCampfireCook =
    _

  deActivate()

  def activate(x: Int, y: Int, equipment: CookingEquipment, fire: TileCampfireCook) {
    active = true
    this.equipment = equipment
    this.fire = fire
    xPos = x
    yPos = y
  }

  def deActivate() {
    active = false
    equipment = null
    fire = null
    xPos = -1000
    yPos = -1000
  }

  override def getSlotStackLimit: Int =
    1

  override def isItemValid(stack: ItemStack): Boolean =
    if ((equipment != null) && (fire != null)) equipment.canCook(stack)
    else false
}

class SlotItem(inv: IInventory, index: Int, x: Int, y: Int, item: Item) extends Slot(inv, index, x, y) with SlotOnly {
  override def getAllowedItems: Vector[Item] =
    Vector(item)
}

class SlotItemMeta(inv: IInventory, index: Int, x: Int, y: Int, item: Item, damage: Vector[Int]) extends Slot(inv, index, x, y) with SlotOnly {
  override def getAllowedStacks: Vector[ItemStack] =
    damage.map(meta => new ItemStack(item, 1, meta))
}

class SlotBackpack(inv: IInventory, index: Int, x:Int, y:Int, active: Boolean) extends Slot(inv, index, x, y) with SlotDisable with SlotNot {
  if(!active) disable()

  override def getBanItems: Vector[Item] =
    Vector(Objs.backpack)
}

class SlotTabbedBackpack(index: Int, x:Int, y:Int, val tabIdLeft: Int, val tabIdTop: Int) extends
  SlotBackpack(null, index, x, y, false) with SlotWithTabs with SlotChangingInventory {

  private var inv: Option[IInventory] = None

  override def getIInventory: Option[IInventory] =
    inv

  override def setIInventory(inventory: Option[IInventory]): Unit =
    inv = inventory
}

class SlotTabbed(inv: IInventory, index: Int, x:Int, y:Int, val tabIdLeft: Int, val tabIdTop: Int) extends
  Slot(inv, index, x, y) with SlotWithTabs

class SlotTabbedCrafting(player: EntityPlayer, craftInv: InventoryCrafting, inv: IInventory, index: Int, x:Int, y:Int, val tabIdLeft: Int, val tabIdTop: Int) extends
  SlotCrafting(player, craftInv, inv, index, x, y) with SlotWithTabs {
}

object SlotArmor {
  val armorSlots = List(HEAD, CHEST, LEGS, FEET)
}

class SlotTabbedArmor(inv: IInventory, player: EntityPlayer, index: Int, x:Int, y:Int, tabLeft: Int, tabTop: Int, armorIndex: Int)
  extends SlotTabbed(inv, index, x, y, tabLeft, tabTop) {

  override def getSlotStackLimit =
    1

  override def isItemValid(stack: ItemStack): Boolean =
    stack.getItem.isValidArmor(stack, SlotArmor.armorSlots(armorIndex), player)

  @SideOnly(Side.CLIENT)
  override def getSlotTexture = ItemArmor.EMPTY_SLOT_NAMES(3 - armorIndex)
}