package my_ksql_udf;

import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import my_ksql_udf.Comp ;

@UdfDescription(name = "comp", description = "comp")
public class CompUDF {
  Object fnObj = null;

  @Udf(description = "default")
  public String invoke() {
    return "larluo" ;
  }
  @Udf(description = "comp")
  public String invoke(final String fnsStr, final String arg) {
    System.out.println("fnsStr: " + fnsStr) ;
    if (fnObj == null) {
      fnObj = Comp.compile(fnsStr);
      System.out.println("fnObj: " + fnObj) ;
    }
    
    return Comp.run(fnObj, arg);
  }

  public static void main(String[] args) {
    String fnsStr = "bs2str.b64-encode.gzip.str2bs";
    String argsStr = "larluo";

    System.out.println(new CompUDF().invoke(fnsStr, argsStr));
  }
}
