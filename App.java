
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import javax.lang.model.util.ElementScanner14;


class Review 
{
    String username;
    String product;
    String message;
    String picture;

    boolean drop = false;

    Review(String username, String product, String message, String picture)
    {
        this.username = username;
        this.product = product;
        this.message = message;
        this.picture = picture;
    }

    @Override
    public String toString() 
    {
        return username + "," + product + "," + message + "," + picture;
    }

    
}


class ProductCustomerDB
{
    ArrayList<ArrayList<String>> ProdCusLists = new ArrayList<ArrayList<String>>();

    ArrayList<String> GetListOfCustomers(int product)
    {
        return ProdCusLists.get(product);
    }

    void AddList(int product, ArrayList<String> List)
    {
        ProdCusLists.add(product, List);
    }

}

abstract class ReviewFilter //implements Runnable
{
    Review review;
    void GetReview(Review review)
    {
        this.review = review;
    }

    void PassMessage(ReviewFilter destinationFilter)
    {
        //if(review == null) return;
        destinationFilter.GetReview(review);
    }

    abstract void executeFilter();
}

class ProfanityFilter extends ReviewFilter
{
    List<String> profanityList;

    public ProfanityFilter(List<String> list)
    {
        this.profanityList = list;
    }

    @Override
    void executeFilter() 
    {
       if(review.drop) return;

       boolean foundProfanity = false;
       String message = review.message;
       //System.out.println(message);

       for (String profanity : profanityList) 
       {
            if(message.contains(profanity))
            {
                foundProfanity = true;
                System.out.println("CONTAINS PROFANITY");

                review.drop = true;
                break;
            }
       }
    }
}

class BuyerCheckFilter extends ReviewFilter
{

    Hashtable<String, List<String>> ProductBuyerTable;

    BuyerCheckFilter(Hashtable<String, List<String>> ProductBuyerTable)
    {
        this.ProductBuyerTable = ProductBuyerTable;
    }

    @Override
    void executeFilter() 
    {
        if(review.drop) return;

        String product = review.product;
        String user = review.username;

        if(!ProductBuyerTable.containsKey(product))
        {
            //System.out.println("no such product");
            review.drop = true;
        }
        else
        {
            List<String> ProductBuyers = ProductBuyerTable.get(product);
            if(!ProductBuyers.contains(user)) review.drop = true;
            //else System.out.println("Works");
        }
    } 
}

class OutputFilter extends ReviewFilter
{
   
    @Override
    void executeFilter() 
    {
        if(review.drop) return;
        System.out.println(review.toString()); 
    }
}

class PropagandaFilter extends ReviewFilter
{
    List<String> propagandaList;

    public PropagandaFilter(List<String> propagandaList) 
    {
        this.propagandaList = propagandaList;
    }

    

    public PropagandaFilter() {
    }

    @Override
    void executeFilter() 
    {
        if(review.drop) return;

        String message = review.message;

        for (String propaganda : propagandaList)
        {
            if(message.contains(propaganda))
            {
                System.out.println("contains " + propaganda);
                String regex = "[" + propaganda + "]";
                message = message.replaceAll(regex, "");
            }
        }

        review.message = message;
    }  
}

class PictureFilter extends ReviewFilter
{

    @Override
    void executeFilter() 
    {
        if(review.drop) return;

        String picture = review.picture;
        picture = picture.toLowerCase();

        review.picture = picture;
    }
    
}

class RemoveCompFilter extends ReviewFilter
{

    @Override
    void executeFilter() 
    {
        if(review.drop) return;

        String message =review.message;
        if(message.contains("http"))
        {
            //System.out.println("CONTAINS HTTP");
            message = message.replaceAll("[http]", "");
        }

        review.message = message;
    }
    
}

class SentimentFilter extends ReviewFilter
{

    @Override
    void executeFilter() 
    {
        if(review.drop) return;

        String message = review.message;
        int len = message.length();

        int sum = 0;

        for(int i = 0; i < len; i++)
        {
            if(Character.isUpperCase((message.charAt(i)))) sum++;
            else                                           sum--;
        }

        if(sum == 0)     message.concat("=");
        else if(sum > 0) message.concat("+");
        else             message.concat("-");
       
    }
    
}


public class App 
{
    public static void main(String[] args) throws Exception 
    {
        //System.out.println("Hello, World!");

        List<String> porfanityList = new ArrayList<String>(10);
        porfanityList.add("@#$%");

        Hashtable<String, List<String>> ProductBuyerTable = new Hashtable<>();

        List<String> LaptopBuyers = new ArrayList<String>();
        LaptopBuyers.add("John");

        ProductBuyerTable.put("Laptop", LaptopBuyers);  

        List<String> PropagandaList = new ArrayList<String>();
        PropagandaList.add("+++");
        PropagandaList.add("---");

        Review m1 = new Review("John", "Laptop", "---o+++k +++", "PICTURE");
        Review m2 = new Review("Mary", "Phone", "httpabcd  @#$%", "IMAGE");

        ProfanityFilter f_prof = new ProfanityFilter(porfanityList);
        OutputFilter f_out = new OutputFilter();
        BuyerCheckFilter f_buyer = new BuyerCheckFilter(ProductBuyerTable);
        PropagandaFilter f_propa = new PropagandaFilter(PropagandaList);
        PictureFilter f_pic = new PictureFilter();
        RemoveCompFilter f_remCom = new RemoveCompFilter();

        f_remCom.GetReview(m2);
        f_remCom.executeFilter();
        System.out.println(m2.message);

        f_pic.GetReview(m2);
        f_pic.executeFilter();
        System.out.println(m2.picture);


        f_propa.GetReview(m1);
        f_propa.executeFilter();
        System.out.println(m1.message);

        f_buyer.GetReview(m2);
        f_buyer.executeFilter();


        f_prof.GetReview(m1);
        f_prof.executeFilter();
        f_prof.PassMessage(f_out);
        f_out.executeFilter();


        f_prof.GetReview(m2);
        f_prof.executeFilter();
        
    }
}
