
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

enum ProductEnum 
{
    LAPTOP,
    BOOK,
    PHONE
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
    String review;
    void GetReview(String review)
    {
        this.review = review;
    }

    void PassMessage(ReviewFilter destinationFilter)
    {
        if(review == null) return;
        destinationFilter.GetReview(review);
    }

    abstract void executeFilter();
}

class ProfanityFilter extends ReviewFilter
{
    List<String> profanityList;

    public ProfanityFilter(List<String> list)
    {
        this.profanityList= list;
    }

    String GetMessage()
    {
        String regex = "[,]";
        String[] splitResult = review.split(regex);

        return splitResult[2];
    }

    @Override
    void executeFilter() 
    {
       if(review == null) return;

       boolean foundProfanity = false;
       String message = GetMessage();
       //System.out.println(message);

       for (String profanity : profanityList) 
       {
            if(message.contains(profanity))
            {
                foundProfanity = true;
                //System.out.println("CONTAINS PROFANITY");

                review = null;
                break;
            }
       }
    }
}

class BuyerCheckFilter extends ReviewFilter
{

    @Override
    void executeFilter() 
    {
        
    }

    String GetProduct()
    {
        String regex = "[,]";
        String[] splitResult = review.split(regex);

        return splitResult[1];
    }
    
    
}





public class App 
{
    public static void main(String[] args) throws Exception 
    {
        //System.out.println("Hello, World!");

        List<String> porfanityList = new ArrayList<String>(10);
        porfanityList.add("@#$%");

        String m1 = "John, Laptop, ok, PICTURE";
        String m2 = "Mary, Phone, @#$%), IMAGE";

        ProfanityFilter f1 = new ProfanityFilter(porfanityList);
        f1.GetReview(m2);
        f1.executeFilter();
    }
}
