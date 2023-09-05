import java.io.*;
import java.util.*;

public class restaurant {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> produtos = new ArrayList<>();
        List<String> vendas = new ArrayList<>();
        int codigoVenda = 1;

        // Carregar produtos do arquivo
        carregarProdutos(produtos);

        // Carregar vendas do arquivo
        carregarVendas(vendas);

        while (true) {
            System.out.println("Bem-vindo ao Restaurante/Lanchonete!");
            System.out.println("1 - Iniciar comanda");
            System.out.println("2 - Sair");
            System.out.println("3 - Listar Clientes e Valores Gastos");
            System.out.print("Escolha uma opção: ");

            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    double totalComanda = 0.0;
                    List<String> itensComanda = new ArrayList<>();

                    while (true) {
                        System.out.println("\nProdutos disponíveis:");
                        for (String produto : produtos) {
                            System.out.println(produto.split("\\|")[0] + " - " + produto.split("\\|")[1]);
                        }

                        System.out.print("Digite o código do produto ou 0 para finalizar a comanda: ");
                        int codigoProduto = scanner.nextInt();

                        if (codigoProduto == 0) {
                            break;
                        }

                        boolean produtoEncontrado = false;
                        for (String produto : produtos) {
                            String[] partes = produto.split("\\|");
                            int codigo = Integer.parseInt(partes[0]);
                            if (codigo == codigoProduto) {
                                double preco = Double.parseDouble(partes[2].replace(',', '.'));
                                totalComanda += preco;
                                itensComanda.add(produto);
                                produtoEncontrado = true;
                                break;
                            }
                        }

                        if (!produtoEncontrado) {
                            System.out.println("Código de produto inválido. Tente novamente.");
                        }
                    }

                    scanner.nextLine(); 
                    System.out.print("Digite o nome do cliente: ");
                    String nomeCliente = scanner.nextLine();

                    String venda = String.format("%02d|%s|%.2f", codigoVenda, nomeCliente, totalComanda);
                    salvarVenda(venda);
                    vendas.add(venda);
                    codigoVenda++;

                    System.out.println("Comanda registrada com sucesso!");
                    System.out.println("Total da comanda: R$" + totalComanda);
                    break;

                case 2:
                    System.out.println("Comming Out");
                    salvarVendas(vendas); 
                    scanner.close();
                    System.exit(0);
                    break;

                case 3:
                    listarClientesEValores(vendas);
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void salvarVenda(String venda) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendas.txt", true))) {
            writer.write(venda);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar venda: " + e.getMessage());
        }
    }

    private static void salvarVendas(List<String> vendas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendas.txt"))) {
            for (String venda : vendas) {
                writer.write(venda);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar vendas: " + e.getMessage());
        }
    }

    private static void listarClientesEValores(List<String> vendas) {
        Map<String, Double> clientesEValores = new HashMap<>();

        for (String venda : vendas) {
            String[] partes = venda.split("\\|");
            if (partes.length >= 3) {
                String nomeCliente = partes[1];
                double valorCompra = Double.parseDouble(partes[2].replace(',', '.'));

                clientesEValores.put(nomeCliente, clientesEValores.getOrDefault(nomeCliente, 0.0) + valorCompra);
            }
        }

        if (clientesEValores.isEmpty()) {
            System.out.println("Nenhum cliente registrado.");
        } else {
            System.out.println("Clientes e Valores Gastos:");
            for (Map.Entry<String, Double> entry : clientesEValores.entrySet()) {
                System.out.println(entry.getKey() + " - R$" + entry.getValue());
            }
        }
    }

    private static void carregarProdutos(List<String> produtos) {
        try (BufferedReader reader = new BufferedReader(new FileReader("produtos.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                produtos.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private static void carregarVendas(List<String> vendas) {
        try (BufferedReader reader = new BufferedReader(new FileReader("vendas.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                vendas.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar vendas: " + e.getMessage());
        }
    }
}
