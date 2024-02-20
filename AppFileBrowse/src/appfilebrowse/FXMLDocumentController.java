/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appfilebrowse;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 *
 * @author silvi
 */
public class FXMLDocumentController implements Initializable {
    
  
    @FXML
    private TableView<Arquivo> tabela;
    @FXML
    private TableColumn<Arquivo, String> colnome;
    @FXML
    private TableColumn<Arquivo, Long> coltam;
    @FXML
    private TableColumn<Arquivo, Boolean> colpasta;
    private String caminhoinicial="D:\\";
        private File pasta=null;
    @FXML
    private Button btsobre;
    @FXML
    private Button btnovapasta;
    @FXML
    private MenuItem apagar;
    @FXML
    private Button btfiltro;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colnome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        coltam.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        colpasta.setCellValueFactory(new PropertyValueFactory<>("pasta"));
        colpasta.setCellFactory(CheckBoxTableCell.forTableColumn(colpasta));
        tabela.setEditable(true);
        preencherTabela();
                colpasta.setCellFactory(new Callback<TableColumn<Arquivo,Boolean>,TableCell<Arquivo,Boolean>>(){
            @Override
            public TableCell<Arquivo, Boolean> call(TableColumn<Arquivo, Boolean> param) {
                return new TableCell<Arquivo,Boolean>()
                {
                 @Override
                 protected void updateItem(Boolean item,boolean empty)
                 {
                    super.updateItem(item, empty);
                    ImageView imgcheck = new ImageView("icones/check.png");
                    ImageView imgnocheck = new ImageView("icones/no-check.png");
                    if(!empty && item)
                        setGraphic(imgcheck);
                    else
                        setGraphic(imgnocheck);
                 }
                };
            }  
        });
                tabela.setRowFactory(tv -> new TableRow<Arquivo>() {
            public void updateItem(Arquivo item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isPasta()) 
                    setStyle("-fx-background-color: #b3b6ba;");
                else 
                    setStyle("");
                
            }
});

    }    
    private void preencherTabela()
    {
        List <Arquivo> arquivos=new ArrayList();
        File pasta=new File(caminhoinicial);
        for (File f : pasta.listFiles())
        {
            arquivos.add(new Arquivo(f.getName(),f.length(),f.isDirectory()));
        }
        tabela.setItems(FXCollections.observableArrayList(arquivos));
    }

    @FXML
    private void evtSobre(ActionEvent event) {
        Arquivo arq;
        String info="selecione um arquivo";
        if(tabela.getSelectionModel().getSelectedIndex()>=0)
        {
            arq=tabela.getSelectionModel().getSelectedItem();
            File aux=new File(caminhoinicial+"\\"+arq.getNome());
            Date date = new Date(aux.lastModified());
            info=arq.getNome()+"\n"+arq.getTamanho()+"\n"+"modificado a "+date.getDate()+" dias";    
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informações sobre o arquivo");
        alert.setContentText(info);
        alert.showAndWait();
    }

    @FXML
    private void evtClickTabela(MouseEvent event) {
        
        if(tabela.getSelectionModel().getSelectedIndex()>=0)
            btsobre.setDisable(false);
        else
            btsobre.setDisable(true);
        
        String nome = tabela.getSelectionModel().getSelectedItem().getNome();
        if(event.getClickCount() == 2) {
            try {
                Desktop.getDesktop().open(new File(caminhoinicial + "/" + nome));
            }
            catch(Exception e) {
                System.out.println("ERRO:" + e.getMessage());
            }
        }
    }

    @FXML
    private void evtNovaPasta(ActionEvent event) {
        String novapasta="";
        TextInputDialog dialog= new TextInputDialog("NovaPasta");
        dialog.setContentText("Informe o nome da nova pasta");
        novapasta=dialog.showAndWait().get();
        if(!novapasta.isEmpty())
        {
            File file=new File(caminhoinicial+"\\"+novapasta);
            if (!file.mkdir())
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Erro ao criar a pasta");
                alert.showAndWait();
            }
            else
                //tabela.getItems().add(new Arquivo(novapasta, 0, true));
                preencherTabela();

        }
    }

    @FXML
    private void evtapagar(ActionEvent event) {
        File remove = new File(caminhoinicial + "/" + tabela.getSelectionModel().getSelectedItem().getNome());
        if(JOptionPane.showConfirmDialog(null, "Deseja realmente exluir " + tabela.getSelectionModel().getSelectedItem().getNome() + "?", "Exluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            remove.delete();
        preencherTabela();
    }

    @FXML
    private void evtFiltro(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Informe o filtro dos arquivos");
        Optional<String> res = dialog.showAndWait();
        if (res != null) {
            File f = null;
            File[] paths;
            f = new File(caminhoinicial);

            FileFilter filter = file -> file.getName().endsWith(res.get()); 

            // returns pathnames for files and directory
            paths = f.listFiles(filter);

            List<Arquivo> arquivos = new ArrayList();
            for (File path : paths) {

                // prints file and directory paths
                arquivos.add(new Arquivo(path.getName(), path.length(), path.isDirectory()));
            }
            tabela.setItems(FXCollections.observableArrayList(arquivos));
        }
        /*/

        }//*/
    }
}
