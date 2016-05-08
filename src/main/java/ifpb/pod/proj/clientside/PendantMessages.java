package ifpb.pod.proj.clientside;

import nu.xom.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vmvini on 07/05/16.
 */
public class PendantMessages {





    private void removeMsg(Map<String, String> map){
        try {
            List<Map<String, String>> list = listarMensagens();
            list.remove(map);
            escreverMensagem(list);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ParsingException e){
            e.printStackTrace();
        }

    }


    public List< Map<String, String> > getAll(){
        try{
            return listarMensagens();
        }
        catch (ParsingException e){
            e.printStackTrace();
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void confirmSendedMessage(Map<String, String> map){
        removeMsg(map);
    }

    public void save(Map<String, String> map){
        try {

            List<Map<String, String>> list = listarMensagens();
            if(list.contains(map))
                return;

            list.add(map);

            escreverMensagem(list);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ParsingException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }


    public void escreverMensagem(List<Map<String, String>> list) {

        Element root = new Element("mensagens");
        for (Map<String, String> map : list) {
            Element mensagemEl = new Element("mensagem");
            Element idEl = new Element("id");
            idEl.appendChild(map.get("id"));
            Element usuarioIdEl = new Element("usuarioId");
            usuarioIdEl.appendChild(map.get("usuarioId"));
            Element dataTimeEl = new Element("dataTime");
            dataTimeEl.appendChild(map.get("dataTime"));
            Element grupoIdEl = new Element("grupoId");
            grupoIdEl.appendChild(map.get("grupoId"));
            Element contentEl = new Element("conteudo");
            contentEl.appendChild(map.get("conteudo"));
            mensagemEl.appendChild(idEl);
            mensagemEl.appendChild(usuarioIdEl);
            mensagemEl.appendChild(dataTimeEl);
            mensagemEl.appendChild(grupoIdEl);
            mensagemEl.appendChild(contentEl);
            root.appendChild(mensagemEl);
        }

        Document doc = new Document(root);

        File usrFile = new File(this.getClass().getResource("pendant_messages.xml").getFile());

        try {
            Serializer serializer = new Serializer(new FileOutputStream(usrFile), "UTF-8");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException ex) {
            System.err.println(ex);
        }


    }


    private List<Map<String, String>> listarMensagens() throws IOException, ParsingException{
        Builder builder = new Builder();
        InputStream is = new FileInputStream(new File(this.getClass().getResource("pendant_messages.xml").getFile()));
        Document doc = builder.build(is);
        Element root = doc.getRootElement();
        Elements childs = root.getChildElements("mensagem");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < childs.size(); i++) {
            Element atual = childs.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            String idValue = atual.getChild(1).getValue();
            String userId = atual.getChild(3).getValue();
            String dataTime = atual.getChild(5).getValue();
            String grupoId = atual.getChild(7).getValue();
            String conteudo = atual.getChild(9).getValue();
            map.put("id", idValue);
            map.put("usuarioId", userId);
            map.put("dataTime", dataTime);
            map.put("grupoId", grupoId);
            map.put("conteudo", conteudo);
            list.add(map);
        }
        return list;
    }

}
