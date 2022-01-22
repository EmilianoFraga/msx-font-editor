#MSX Font Editor - Versão 1.6.1

##0- Novidades

1.7.0 - Refatoração para usar Apache Maven e nova estrutura de pacotes

1.6.1 - Corrigido bug que não reconhece arrasto do mouse como o botão direito no painel de caracteres em alguns Java's.

1.6 - Nova ferramenta de busca de fontes em arquivos, códigos ASCII de referência e correção de bugs.

1.5 - Agora lida com cores da screen 1, lê/grava em diversos formatos, adicionado tutorial do mouse.

1.4 - Ferramentas de deslocamento de caractere, duplo clique para copiar uma letra para editor e voltar.

1.3 - Seleção múltipla de caracteres, arrasto de mouse para selecionar caracteres, possível copiar/colar vários cracateres entre fontes.

1.2 - Melhorias na interface gráfica

1.1 - Correção de bugs


##1- Compilando e executando o MSX Font Editor

O MSX Font Editor foi criado no ambiente Java, no qual é compatível com diversos sistemas operacionais como Windows, Linux e Mac.
Não é necessário recompilar o código executável do programa para rodar em outro sistema operacional, porque aplicativos Java são multi-plataforma.

###1.1. Requisitos
* Ao menos JDK 11 instalado:
https://jdk.java.net/java-se-ri/11
* Ao menos least Apache Maven 3.6.4
https://maven.apache.org/download.cgi

###1.2. Compilando
* No diretório raiz do projeto, digite na linha de comando:

```mvn clean package```

Maven irá criar um arquivo `.jar` no diretório `target`.
ão do arquivo comprimido de ".zip" para ".jar".

###1.3. Rodando o arquvo ".jar"
* Linux

```java -jar target/mfe-1.7.0.jar```

* Windows
```
java -jar target\mfe-1.7.0.jar
```


##2- Página do MSX Font Editor e contato:
###Versão 1.7.0:

Autor: Emiliano Vaz Fraga

E-mail: efraga@gmail.com

###Até versão 1.6.1
Autor: Marcelo Teixeira Silveira

Homepage: marmsx.msxall.com

E-mail: flamar98@hotmail.com
