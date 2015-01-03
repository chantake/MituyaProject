/*
 * MituyaProject
 * Copyright (C) 2011-2015 chantake <http://328mss.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.chantake.MituyaProject.World.Shop;

/**
 *
 * @author fumitti
 */
public class RemoteChestShop {

    /**
     * public static void buy(final MituyaProject plugin, final PlayerInstance pi, final MaterialData md, final int amount) throws PlayerOfflineException { if
     * (ChestShop.getInstance().getBuy().containsKey(md)) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "購入処理中です。しばらくお待ち下さい。。。");
     *
     * new Thread() {
     *
     * int amt = amount; HashMap<String, Integer[]> buyers = new HashMap<>();
     *
     * @Override public void run() { ArrayList<ChestShopData> get = ChestShop.getInstance().getBuy().get(md); synchronized (get) {
     * ChestShop.getInstance().sortbuyChestShopDataArrayList(get); for (ChestShopData csd : get) { if (amt < 1) { break; } if
     * (ShopRate.getItemPriceBuy(md.getItemTypeId(), md.getData())) { if (ShopRate.getItemBuy(md.getItemTypeId(), md.getData())>0) { if
     * (ShopRate.getItemBuy(md.getItemTypeId(), md.getData()) * 1.1 < (csd.getBuy() / csd.getAmount())) { try { ChestShop.getInstance().oldbuy(pi,
     * md.getItemTypeId(), md.getData(), amount, buyers); amt = 0; break; } catch (Exception ex) { } } } } try { int retint =
     * ChestShop.getInstance().cmdBuy(plugin, csd, pi, buyers, amt); if (retint == -1) { break; } else { amt -= retint; } sleep(100); } catch
     * (InterruptedException | PlayerOfflineException | NullPointerException ex) { break; } } } int allam = 0; int allmn = 0; for (Map.Entry<String, Integer[]>
     * buyer : buyers.entrySet()) { String string = buyer.getKey(); Integer[] integers = buyer.getValue(); if (integers[0] == 0) { break; } allam +=
     * integers[0]; allmn += integers[1]; PlayerInstance owner = plugin.getInstanceManager().getInstance(string); try { pi.sendInfo(ChatColor.RED + "Shop",
     * ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様から、" + ChatColor.AQUA + md.getItemType().name() + ChatColor.WHITE + " を " + ChatColor.AQUA +
     * integers[0] + ChatColor.WHITE + " 個購入しました。(" + integers[1] + "Mine)"); //owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() +
     * ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。"); owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + pi.getName() +
     * ChatColor.WHITE + " 様に、" + ChatColor.AQUA + md.getItemType().name() + ChatColor.WHITE + " を " + ChatColor.AQUA + integers[0] + ChatColor.WHITE + "
     * 個売却しました。(" + integers[1] + "Mine)"); } catch (Exception e) { } } pi.sendInfo(ChatColor.RED + "Shop", ChatColor.WHITE + "合計 " + ChatColor.AQUA +
     * Integer.toString(buyers.size()) + ChatColor.WHITE + " 名の方から " + ChatColor.AQUA + allam + ChatColor.WHITE + " 個購入しました。(" + allmn + "Mine)");
     *
     * if (amt != 0) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "一部のみ購入できたっぽいです"); } } }.start(); } else { pi.sendInfo(ChatColor.RED + "Shop",
     * ChatColor.YELLOW + "販売者が居ません"); } }
     *
     * public static void sell(final MituyaProject plugin, final PlayerInstance pi, final MaterialData md, final int amount) throws PlayerOfflineException { if
     * (ChestShop.getInstance().getSell().containsKey(md)) { new Thread() {
     *
     * int amt = amount; HashMap<String, Integer[]> sellers = new HashMap<>();
     *
     * @Override public void run() { ArrayList<ChestShopData> get = ChestShop.getInstance().getSell().get(md);
     *
     * synchronized (get) { ChestShop.getInstance().sortsellChestShopDataArrayList(get);
     *
     * pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "売却処理中です。しばらくお待ち下さい。。。"); for (ChestShopData csd : get) { if (amt < 1) { break; } if
     * (ShopRate.getItemPriceSell(md.getItemTypeId(), md.getData())) { if (ShopRate.getItemSell(md.getItemTypeId(), md.getData())>0) { if
     * (ShopRate.getItemSell(md.getItemTypeId(), md.getData()) * 0.9 > (csd.getSell() / csd.getAmount())) { try { ChestShop.getInstance().oldsell(pi,
     * md.getItemTypeId(), md.getData(), amount, sellers); amt = 0; break; } catch (Exception ex) { } } } } try { int retint =
     * ChestShop.getInstance().cmdSell(plugin, csd, pi, sellers, amt); if (retint == -1) { break; } else { amt -= retint; } sleep(100); } catch
     * (InterruptedException | PlayerOfflineException | NullPointerException ex) { break; } } } int allam = 0; int allmn = 0; for (Map.Entry<String, Integer[]>
     * seller : sellers.entrySet()) { String string = seller.getKey(); Integer[] integers = seller.getValue(); if (integers[0] == 0) { break; } allam +=
     * integers[0]; allmn += integers[1]; PlayerInstance owner = plugin.getInstanceManager().getInstance(string); try { pi.sendInfo(ChatColor.RED + "Shop",
     * ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様に、" + ChatColor.AQUA + md.getItemType().name() + ChatColor.WHITE + " を " + ChatColor.AQUA +
     * integers[0] + ChatColor.WHITE + " 個売却しました。(" + integers[1] + "Mine)"); //owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() +
     * ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。"); owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + pi.getName() +
     * ChatColor.WHITE + " 様から、" + ChatColor.AQUA + md.getItemType().name() + ChatColor.WHITE + " を " + ChatColor.AQUA + integers[0] + ChatColor.WHITE + "
     * 個買取しました。(" + integers[1] + "Mine)"); } catch (Exception e) { } } pi.sendInfo(ChatColor.RED + "Shop", ChatColor.WHITE + "合計 " + ChatColor.AQUA +
     * Integer.toString(sellers.size()) + ChatColor.WHITE + " 名の方に、" + ChatColor.AQUA + md.getItemType().name() + ChatColor.WHITE + " を " + ChatColor.AQUA +
     * allam + ChatColor.WHITE + " 個売却しました。(" + allmn + "Mine)");
     *
     * if (amt != 0) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "一部のみ売却できたっぽいです"); } } }.start(); } else { pi.sendInfo(ChatColor.RED + "Shop",
     * ChatColor.YELLOW + "購入者が居ません"); } }
     *
     * public static void priceVerbose(MituyaProject plugin, final PlayerInstance pi, final MaterialData md) { pi.sendInfo(ChatColor.RED + "Shop",
     * ChatColor.YELLOW + "価格状況を調査中です。しばらくお待ち下さい。売りはx0.9 買いはx1.1"); pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "処理中に他の価格調査を実行しないでください。");
     *
     * new Thread() {
     *
     * @Override public void run() { int maxbuy = 0; int minbuy = 0; int minbuyam = 0; int buyamount = 0; int avrbuy = 0; int minsell = 0; int maxsell = 0; int
     * sellamount = 0; int avrsell = 0; String c = ""; String ret = "[" + ChatColor.RED + "Shop" + ChatColor.WHITE + "] ";//なんか文作る String buy, sell; int notice
     * = 100; int now = 0; if (ChestShop.getInstance().getBuy().containsKey(md)) { ArrayList<ChestShopData> buyArray = ChestShop.getInstance().getBuy().get(md);
     * synchronized (buyArray) { ChestShop.getInstance().sortbuyChestShopDataArrayList(buyArray); minbuy = buyArray.get(0).getBuy(); maxbuy =
     * buyArray.get(buyArray.size() - 1).getBuy(); pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "売り状況を調査中。。。調査件数:0/" + buyArray.size()); for
     * (ChestShopData bsd : buyArray) { try { Inventory inventory = ChestShop.getInstance().getShopChest(bsd.getSign()).getInventory(); ItemStack item =
     * bsd.getItem(); int id = item.getTypeId(); short data = item.getDurability(); int amount = bsd.getAmount(); //チェスト内の在庫チェック for (int i = 0; i <
     * inventory.getSize(); i++) { ItemStack is = inventory.getItem(i); if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() ==
     * data) { if (inventory.getItem(i).getAmount() > 0) { buyamount += inventory.getItem(i).getAmount(); int tempbuy = (bsd.getBuy() / bsd.getAmount()); if
     * (tempbuy == 0) { tempbuy++; } avrbuy += tempbuy * inventory.getItem(i).getAmount(); } } } Chest neighbor =
     * ChestShop.getInstance().getNeighborChest(ChestShop.getInstance().getShopChest(bsd.getSign())); if (neighbor != null) { try { sleep(100); } catch
     * (InterruptedException ex) { } inventory = neighbor.getInventory(); //チェスト内の在庫チェック for (int i = 0; i < inventory.getSize(); i++) { ItemStack is =
     * inventory.getItem(i); if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) { if (inventory.getItem(i).getAmount() >
     * 0) { buyamount += inventory.getItem(i).getAmount(); int tempbuy = (bsd.getBuy() / bsd.getAmount()); if (tempbuy == 0) { tempbuy++; } avrbuy += tempbuy *
     * inventory.getItem(i).getAmount(); } } } } try { sleep(500); } catch (InterruptedException ex) { } } catch (NullPointerException ex) { break; } notice--;
     * if (notice == 0) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "売り状況を調査中。。。調査件数:" + now + "/" + buyArray.size()); notice = 100; } } } try {
     * avrbuy = avrbuy / buyamount; } catch (ArithmeticException e) { avrbuy = 0; } if (ShopRate.getItemPriceBuy(md.getItemTypeId(), md.getData())) { int cshop
     * = ShopRate.getItemBuy(md.getItemTypeId(), md.getData()); if (minbuy > cshop) { minbuy = cshop; } if (maxbuy > cshop) { maxbuy = cshop; } c = "(c有)"; }
     * buy = ret + "[Buy]最安:" + minbuy + "mine,最高:" + maxbuy + "mine,平均:" + avrbuy + "mine,売り個数:" + buyamount + c; } else { if
     * (ShopRate.getItemPriceBuy(md.getItemTypeId(), md.getData())) { buy = ret + "[Buy]売り手cのみ" + ShopRate.getItemBuy(md.getItemTypeId(), md.getData()) +
     * "mine"; } else { buy = ret + "[Buy]売り手無"; } } notice = 100; now = 0; if (ChestShop.getInstance().getSell().containsKey(md)) { ArrayList<ChestShopData>
     * sellArray = ChestShop.getInstance().getSell().get(md); synchronized (sellArray) { ChestShop.getInstance().sortsellChestShopDataArrayList(sellArray);
     * maxsell = sellArray.get(0).getSell(); minsell = sellArray.get(sellArray.size() - 1).getSell(); for (ChestShopData ssd : sellArray) {
     * pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "買い状況を調査中。。。調査件数:0/" + sellArray.size()); try { Chest chest =
     * ChestShop.getInstance().getShopChest(ssd.getSign()); Inventory cinventory = chest.getInventory(); ItemStack item = ssd.getItem(); int amount =
     * ssd.getAmount(); item.setAmount(amount); if (ChestShop.getInstance().getInventoryCost(cinventory, item) < amount) { try { sleep(100); } catch
     * (InterruptedException ex) { } Chest neighbor = ChestShop.getInstance().getNeighborChest(chest); if (neighbor != null &&
     * ChestShop.getInstance().getInventoryCost(neighbor.getInventory(), item) < amount) { } else { sellamount++; avrsell += ssd.getSell(); } } else {
     * sellamount++; avrsell += ssd.getSell(); } try { sleep(500); } catch (InterruptedException ex) { } } catch (NullPointerException ex) { break; } notice--;
     * if (notice == 0) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "買い状況を調査中。。。調査件数:0/" + sellArray.size()); notice = 100; } }
     *
     * }
     * try { avrsell = avrsell / sellamount; } catch (ArithmeticException e) { avrsell = 0; } c = ""; if (ShopRate.getItemPriceSell(md.getItemTypeId(),
     * md.getData())) { int cshop = ShopRate.getItemSell(md.getItemTypeId(), md.getData()); if (minsell > cshop) { minsell = cshop; } if (maxsell > cshop) {
     * maxsell = cshop; c = "(c有)"; } } sell = ret + "[Sell]最高:" + maxsell + "mine,最安:" + minsell + "mine,平均:" + avrsell + "mine,買手数:" + sellamount + c; } else
     * { if (ShopRate.getItemPriceSell(md.getItemTypeId(), md.getData())) { sell = ret + "[Sell]買い手cのみ" + ShopRate.getItemSell(md.getItemTypeId(), md.getData())
     * + "mine"; } else { sell = ret + "[Sell]買い手無"; } } pi.sendMessage(buy); pi.sendMessage(sell); } }.start(); }
     *
     * public static void price(MituyaProject plugin, final PlayerInstance pi, final MaterialData md) { pi.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW +
     * "簡易価格状況を調査中です。");
     *
     * new Thread() {
     *
     * @Override public void run() { int maxbuy = 0; int minbuy = 0; int buyamount = 0; int avrbuy = 0; int minsell = 0; int maxsell = 0; int sellamount = 0;
     * int avrsell = 0; String ret = "[" + ChatColor.RED + "Shop" + ChatColor.WHITE + "] ";//なんか文作る String buy, sell; int notice = 100; int now = 0; String c =
     * ""; if (ChestShop.getInstance().getBuy().containsKey(md)) { ArrayList<ChestShopData> buyArray = ChestShop.getInstance().getBuy().get(md); synchronized
     * (buyArray) { ChestShop.getInstance().sortbuyChestShopDataArrayList(buyArray); minbuy = (int)(buyArray.get(0).getBuy() * 1.1 /
     * buyArray.get(0).getAmount()); maxbuy = (int)(buyArray.get(buyArray.size() - 1).getBuy() * 1.1 / buyArray.get(buyArray.size() - 1).getAmount()); } if
     * (minbuy == 0) { minbuy++; } if (maxbuy == 0) { maxbuy++; } if (ShopRate.getItemPriceBuy(md.getItemTypeId(), md.getData())) { int cshop =
     * ShopRate.getItemBuy(md.getItemTypeId(), md.getData()); if (minbuy > cshop) { minbuy = cshop; } if (maxbuy > cshop) { maxbuy = cshop; } c = "(c有)"; } buy
     * = ret + "[Buy]最安:" + minbuy + "mine,最高:" + maxbuy + "mine,平均:" + avrbuy + "mine,売り個数:" + buyamount + c; } else { if
     * (ShopRate.getItemPriceBuy(md.getItemTypeId(), md.getData())) { buy = ret + "[Buy]売り手cのみ" + ShopRate.getItemBuy(md.getItemTypeId(), md.getData()) +
     * "mine"; } else { buy = ret + "[Buy]売り手無"; } } notice = 100; now = 0; if (ChestShop.getInstance().getSell().containsKey(md)) { ArrayList<ChestShopData>
     * sellArray = ChestShop.getInstance().getSell().get(md); synchronized (sellArray) { ChestShop.getInstance().sortsellChestShopDataArrayList(sellArray);
     * maxsell = (int)(sellArray.get(0).getSell() * 0.9 / sellArray.get(0).getAmount()); minsell = (int)(sellArray.get(sellArray.size() - 1).getSell() * 0.9 /
     * sellArray.get(sellArray.size() - 1).getAmount()); } if (minsell == 0) { minsell++; } if (maxsell == 0) { maxsell++; } c = ""; if
     * (ShopRate.getItemPriceSell(md.getItemTypeId(), md.getData())) { int cshop = ShopRate.getItemSell(md.getItemTypeId(), md.getData()); if (minsell > cshop)
     * { minsell = cshop; } if (maxsell > cshop) { maxsell = cshop; c = "(c有)"; } } sell = ret + "[Sell]最高:" + maxsell + "mine,最安:" + minsell + "mine,平均:" +
     * avrsell + "mine,買手数:" + sellamount + c; } else { if (ShopRate.getItemPriceSell(md.getItemTypeId(), md.getData())) { sell = ret + "[Sell]買い手cのみ" +
     * ShopRate.getItemSell(md.getItemTypeId(), md.getData()) + "mine"; } else { sell = ret + "[Sell]買い手無"; } } pi.sendMessage(buy); pi.sendMessage(sell); }
     * }.start();
    }*
     */
}
