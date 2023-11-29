package seminars.second.simple_shopping_cart;

import jdk.jfr.Name;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    Shop shop;
    Cart cart;

    @BeforeEach
    void setUp() {
        shop = new Shop(getStoreItems());
        cart = new Cart(shop);
    }


    // ������� ����� ��������� ��� ��������:
    public static List<Product> getStoreItems() {
        List<Product> products = new ArrayList<>();

        // ��� ������� ��������, ����, ���-��
        String[] productNames = {"bacon", "beef", "ham", "salmon", "carrot", "potato", "onion", "apple", "melon", "rice", "eggs", "yogurt"};
        Double[] productPrice = {170.00d, 250.00d, 200.00d, 150.00d, 15.00d, 30.00d, 20.00d, 59.00d, 88.00d, 100.00d, 80.00d, 55.00d};
        Integer[] stock = {10, 10, 10, 10, 10, 10, 10, 70, 13, 30, 40, 60};

        // ��������������� ��������� ������ ����������
        for (int i = 0; i < productNames.length; i++) {
            products.add(new Product(i + 1, productNames[i], productPrice[i], stock[i]));
        }

        // ���� �����
        // Product product = new Product(1,"bacon", 170.00d, 10);
        // products.add(product);
        return products;
    }

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    // private Shop shop;
    // private Cart cart;
    //  @BeforeEach
    //  void setup() {
    //      shop = new Shop(getStoreItems());
    //      cart = new Cart(shop);
    //  }


/*
            ID | ��������  | ����, �. | ���-�� � ��������, ��.
            1  | bacon     | 170.0    | 10
            2  | beef      | 250.0    | 10
            3  | ham       | 200.0    | 10
            4  | salmon    | 150.0    | 10
            5  | carrot    | 15.0     | 10
            6  | potato    | 30.0     | 10
            7  | onion     | 20.0     | 10
            8  | apple     | 59.0     | 70
            9  | melon     | 88.0     | 13
            10 | rice      | 100.0    | 30
            11 | eggs      | 80.0     | 40
            12 | yogurt    | 55.0     | 60
*/

    /**
     * 2.1. ������������ ��������� ���� ��� ��������, ��� ����� ���������
     * ������� � ������� �������� ��������� ��������������
     * <br><b>��������� ���������:</b>
     * ��������� ������� ����������� ���������
     */
    @Test
    void priceCartIsCorrectCalculated() {
        // Arrange (����������)

        // Act (����������)
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(2);
        // Assert (�������� �����������)
        assertThat(cart.getTotalPrice()).isEqualTo(170+250);
    }

    /**
     * 2.2. �������� ��������� ���� ��� ��������, ��� ����� ���������
     * ������� � �������������� ������������ ������ � ���� �� �������� ��������� ��������������.
     * <br><b>��������� ���������:</b>
     * ��������� ������� ����������� ���������
     */
    @Test
    void priceCartProductsSameTypeIsCorrectCalculated() {
        // Act
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);
        // Assert
        assertThat(cart.getTotalPrice()).isEqualTo(170*3);
    }

    /**
     * 2.3. �������� ��������� ���� ��� ��������, ��� ��� ��������
     * ������ �� ������� ���������� ���������� ����� ��������� �������.
     * <br><b>��������� ���������:</b>
     * ���������� ����� ��������� ��������� �������, ��������� ������� ��������
     */
    @Test
    void whenChangingCartCostRecalculationIsCalled() {
        // Act
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);
        cart.removeProductByID(1);

        // Assert
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(cart.getTotalPrice()).isEqualTo(340);
    }

    /**
     * 2.4. ������������ ��������� ���� ��� ��������, ��� ��� ���������� ������������� ���������� ������ � �������,
     * ����� ���������� ����� ������ � �������� ��������������� ������� �����������.
     * <br><b>��������� ���������:</b>
     * ���������� ������ � �������� ����������� �� ����� ��������� � ������� ������������
     */

    @Test
    void quantityProductsStoreChanging() {

        // Act
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);

        // Assert
        assertThat(shop.getProductsShop().get(0).getQuantity()).isEqualTo(8);
    }

    /**
     * 2.5. �������� ��������� ���� ��� ��������, ��� ���� ������������ �������� ��� ��������� �������� �
     * ������������ ���� �� ��������, ��� �������� ������ �� �������� ��� ������.
     * <br><b>��������� ���������:</b>
     * ������ ����� ������� �������� ������, �� �� ���������� �� �����
     */
    @Test
    void lastProductsDisappearFromStore() {

        // Act
        for (int i = 0; i < 10; i++) {
            cart.addProductToCartByID(1);
        }
        System.setOut(new PrintStream(output));
        cart.addProductToCartByID(1);
        // Assert
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(10);

        assertThat(output.toString().trim()).isEqualTo("�\u00ADтого товара нет в наличии");
    }

    /**
     * 2.6. �������� ��������� ���� ��� ��������, ��� ��� �������� ������ �� �������,
     * ����� ���������� ����� ������ � �������� ��������������� ������� �������������.
     * <br><b>��������� ���������:</b>
     * ���������� ��������� ����� ���� �� ������ ������������� �� ����� ��������� �� ������� ���������
     */
    @Test
    void deletedProductIsReturnedToShop() {

        // Act
        cart.addProductToCartByID(1);
        cart.addProductToCartByID(1);
        cart.removeProductByID(1);

        // Assert
        assertThat(shop.getProductsShop().get(0).getQuantity()).isEqualTo(9);
    }

    /**
     * 2.7. ������������ ����������������� ��������� ���� ��� ��������,
     * ��� ��� ����� ��������� �������������� ������ ������������ ���������� RuntimeException.
     * <br><b>��������� ���������:</b>
     * ���������� ���� RuntimeException � ��������� �� ������ ������� � id
     * *������� ���� �����������������
     */

    @ParameterizedTest
    @ValueSource (ints = {15, 56, 33})
    void incorrectProductSelectionCausesException(int id) {

        // Act


        // Assert
        assertThatThrownBy(()->cart.addProductToCartByID(id)).isInstanceOf(RuntimeException.class);
    }

    /**
     * 2.8.      * 2.8. �������� ��������� ���� ��� ��������, ��� ��� ������� ������� �� ������� ������ �������,
     * ��� ��� ����, ������������ ���������� RuntimeException.������� �������� �� ����, ��� �� ��������)
     * <br><b>��������� ���������:</b> ���������� ���� NoSuchFieldError � ��������� "� ������� �� ������ ������� � id"
     */

    @Test
    void incorrectProductRemoveCausesException() {

        // Act
        cart.addProductToCartByID(1);
        cart.removeProductByID(1);
        // Assert
        assertThatThrownBy(()->cart.removeProductByID(1)).isInstanceOf(RuntimeException.class);
    }

    /**
     * 2.9. ����� ������������ ����
     */
    // boolean ���������-����() {
    //          // Assert (�������� �����������)
    //          assertThat(cart.getTotalPrice()).isEqualTo(cart.getTotalPrice());
    //          // Act (����������)
    //          cart.addProductToCartByID(2); // 250
    //          cart.addProductToCartByID(2); // 250
    //          // Arrange (����������)
    //          Shop shop = new Shop(getStoreItems());
    //          Cart cart = new Cart(shop);
    //      }

    @Test
    void testSUM() {


        cart.addProductToCartByID(2); // 250
        cart.addProductToCartByID(2); // 250

        assertThat(cart.getTotalPrice()).isEqualTo(500);
    }

    /**
     * 2.10. ����� �������������� �������� �����, �������� ��������� ��������:
     * <br> 1. ������������ ��� - "Advanced test for calculating TotalPrice"
     * <br> 2. ���� ����������� 10 ���
     * <br> 3. ���������� ������� �� ���������� ����� 70 ����������� (unit = TimeUnit.MILLISECONDS)
     * <br> 4. ����� �������� ����������������� �����, ��� ����� ���������
     */
    @Test
    @DisplayName("Advanced test for calculating TotalPrice")
    @RepeatedTest(10)
    @Timeout(value = 70)
    @Disabled
    void gameTest() {

        cart.addProductToCartByID(2); // 250
        cart.addProductToCartByID(2); // 250

        assertThat(cart.getTotalPrice()).isEqualTo(500);
    }
}