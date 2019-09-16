import java.util.*;
import java.io.*;

class CodeChef
{
	public static void main(String[] args) {

		ShopManager manager = new ShopManager();
		manager.shop.addItem(new Item("milk", 3.97, 5.00, 2, 10));
		manager.shop.addItem(new Item("bread", 2.17, 6.00, 3, 10));
		manager.shop.addItem(new Item("apple", 0.89, 10));
		manager.shop.addItem(new Item("banana", 0.99, 10));
        
        Cart cart = new Cart();
		
		cart.addToCart("milk", 3);
		cart.addToCart("bread", 4);
		cart.addToCart("banana", 1);
		cart.addToCart("apple", 0);
        
		//System.out.println(manager.calculator(cart.itemlist_of_customer));
		System.out.println(cart.totalItemsInCart());
		cart.removeFromCart("apple");
		System.out.println(cart.totalItemsInCart());

		manager.shop.Notification();

	}
}

/*
* This is item class which corresponds to real life items that can be 
* available in a general store
*/
class Item
{
	//// general attributes that an item can have
	String name;
	double retail_price;		/// retail price per unit
	double available_quantity;	/// total available quantity of the item in the store
	int onSale;			  		/// whether item is available for thok sale
	double thok_quantity; 		//  if yes, then what is the chunk of quantity on which thok price will be available
	double thok_price;    		///if item is available on thok sale, what is the price of that much thok quantity
	
	
	////// Item object contructors ////////
	Item(String name,
		 double retail_price,
		 double thok_price,
		 double thok_quantity,
		 double available_quantity)
	{
	    this.onSale = 1;
		this.name = name;
		this.retail_price = retail_price;
		this.thok_price = thok_price;
		this.thok_quantity = thok_quantity;
		this.available_quantity = available_quantity;
	}

	Item(String name,
		 double retail_price,
		 double available_quantity)
	{
	    this.onSale = 0;
		this.name = name;
		this.retail_price = retail_price;
		this.available_quantity = available_quantity;
	}

	////////////// update methods available for Item objects /////////////
	void updateRetailPrice(double retail_price)
	{
		this.retail_price = retail_price;
	}

	void updateThokPrice(double thok_price)
	{
		this.thok_price = thok_price;
	}

	void updateThokQuantity(double thok_quantity)
	{
		this.thok_quantity = thok_quantity;
	}

	void updateAvailableQuantity(double sold_quantity)
	{
		this.available_quantity = this.available_quantity - sold_quantity;
	}

	void makeItOnSale()
	{
		this.onSale = 1;
	}


	/*
	* As Item object is going to be added in set and map, we must override hashCode() and equals()
	* methods accordingly, because we need comparison between objects as well as hash value of each
	* object in order to add objects into set or map or any hashing related data structure
	*/
	public int hashCode()
	{
		return this.name.hashCode();
	}

	public boolean equals(Object obj)
	{
		Item item = (Item) obj;
		if(this.name.equals(item.name))
			return true;
		else
			return false;
	}

}

class Shop
{
	Set<Item> available_list;

	Shop()
	{
		available_list = new HashSet<Item>();
	}

	void addItem(Item item)
	{
		available_list.add(item);
	}

	void addItem(String name,
					 double retail_price,
					 double thok_price,
					 double thok_quantity,
					 double available_quantity)
	{
		Item item = new Item(name, retail_price, thok_price, thok_quantity, available_quantity);
		available_list.add(item);
	}

	void addItem(String name,
					 double retail_price,
					 double available_quantity)
	{
		Item item = new Item( name, retail_price, available_quantity);
		available_list.add(item);
	}

	int getItemCountAvailableInShop()
	{
		return available_list.size();
	}

	void listOutAllAvailableItemInShop()
	{
		Iterator itr = available_list.iterator();
		while(itr.hasNext())
		{
			Item item = (Item) itr.next();
			System.out.println(item.name);
		}
	}

	void Notification()
	{
		System.out.println("Availability of items in the store");
		System.out.println();
		Iterator itr = available_list.iterator();
		boolean all_good_flag = true;
		while(itr.hasNext())
		{
			Item item = (Item)itr.next();
			if(item.available_quantity <= 2 && item.available_quantity > 0){
				System.out.println(item.name +"--------"+ item.available_quantity);
			    all_good_flag = false;
			}
			else if(item.available_quantity == 0){
			    System.out.println(item.name + "--------finished in store");
			    all_good_flag = false;
			}
		}
		
		if(all_good_flag)
		    System.out.println("Everything is in sufficient amount");
	}

	void removeItem(String name)
	{
		Iterator itr = available_list.iterator();
		while(itr.hasNext())
		{
			if(((Item)itr.next()).name.equals(name))
				itr.remove();
		}
	}

}


class Cart
{
	Map<String, Integer> itemlist_of_customer;

	Cart()
	{
		itemlist_of_customer = new HashMap<String, Integer>();
	}

	void addToCart(String item_name, int quantity)
	{
		if(quantity>0)
			itemlist_of_customer.put(item_name, quantity);
	}

	void removeFromCart(String item_name)
	{
		itemlist_of_customer.remove(item_name);
	}

	void changeQuantity(String item_name, int additional_quantity)
	{
		int newVal = itemlist_of_customer.get(item_name) + additional_quantity;

		if(newVal > 0)
			itemlist_of_customer.put(item_name, newVal);
		else
			itemlist_of_customer.remove(item_name);
	}

	int totalItemsInCart()
	{
		return itemlist_of_customer.size();
	}
}

class ShopManager
{

	Shop shop;

	ShopManager()
	{
		shop = new Shop();
	}

	double calculator(Map<String, Integer> hm)
	{
		double total = 0.00;
		Iterator<Map.Entry<String, Integer>> itr_map = hm.entrySet().iterator();
		while(itr_map.hasNext())
		{
			Map.Entry<String, Integer> bought_item = itr_map.next();

			double item_price;

			Iterator itr_itemlist = shop.available_list.iterator();
			Item item = null;
			while(itr_itemlist.hasNext())
			{
				item = (Item)itr_itemlist.next();
				if(item.name.equals(bought_item.getKey()))
					break;
			}

			item_price = bought_item.getValue() * item.retail_price;
			item.updateAvailableQuantity(bought_item.getValue());
			System.out.println(bought_item.getKey() +"--------" + bought_item.getValue() + "------" + item_price);
			total = total + item_price;
		}

		return total;
	}



	double calculator(Map<String, Integer> hm, int flag)
	{
		double total = 0.00;
		Iterator<Map.Entry<String, Integer>> itr_map = hm.entrySet().iterator();
		while(itr_map.hasNext())
		{
			Map.Entry<String, Integer> bought_item = itr_map.next();

			double item_price;

			Iterator itr_itemlist = shop.available_list.iterator();
			Item item = null;
			while(itr_itemlist.hasNext())
			{
				item = (Item)itr_itemlist.next();
				if(item.name.equals(bought_item.getKey()))
					break;
			}
            
            item.updateAvailableQuantity(bought_item.getValue());
            if(item.onSale == 1){
			    item_price = ((bought_item.getValue()/(int)item.thok_quantity) * item.thok_price) +  ((bought_item.getValue()%(int)item.thok_quantity) * item.retail_price);
                System.out.println(bought_item.getKey() +"--------"+ bought_item.getValue() +"------" + item_price);
            }
			else{
			    item_price = bought_item.getValue() * item.retail_price;
			    System.out.println(item.name +"--------"+ bought_item.getValue() +"------" + item_price);
			}
			
			total = total + item_price;
		}
		
		return total;
	}
}