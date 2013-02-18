## Purpose ##
Provide a simple acceptance testing framework usable by product managers and developers likewise

## Setup and run ##
1. git clone https://git@github.com:rwirdemann/org.jcriteria.git
1. Create a new maven bases Java Project
1. in src/test/resources/stories/transfer_money.story
1. in src/test/stories/TransferMoney.java

<pre>
<code>
@RunWith(JCriteriaRunner.class)
public class TransferMoney {

    @Criteria("Should run criteria")
    public void shouldRunCriteria() {
        Assert.assertTrue(true);
    }
}
</code>
</pre>