package com.example.myapplication.NaturalLanguageProcessing;

import java.util.Arrays;
import java.util.List;

public class NaturalLanguageProcessingImplSpanish implements NaturalLanguageProcessing {

    @Override
    public boolean isCreateTask(String spokenText) { // crear [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 1 && words.get(0).equals("crear");
    }

    @Override
    public boolean isDeleteTask(String spokenText) { // eliminar [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return !isDeleteAllTask(spokenText) && words.size() >= 2 && words.get(0).equals("eliminar");
    }

    @Override
    public boolean isUpdateTask(String spokenText) { // modificar [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("modificar");
    }

    @Override
    public boolean isGoToTask(String spokenText) { // tareas
        spokenText = this.normalizeText(spokenText);
        return spokenText.equals("tarea") || spokenText.equals("tareas");
    }

    @Override
    public boolean isDeleteAllTask(String spokenText) { // eliminar todo
        spokenText = this.normalizeText(spokenText);
        return spokenText.equals("eliminar todo");
    }


    @Override
    public boolean isTaskDone(String spokenText) { // tarea completada [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 3 && words.get(0).equals("tarea") && words.get(1).equals("completada");
    }

    @Override
    public boolean isTaskNotDone(String spokenText) { // tarea no completada [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 4 && words.get(0).equals("tarea") && words.get(1).equals("no") &&  words.get(2).equals("completada");
    }

    /**
     *
     * @param spokenText The spokenText is a valid text with spokenText.split(" ").lenght >= 2
     * @return Returns the taskID
     */
    @Override
    public String getTaskIdFromText(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        if(isTaskDone(spokenText)) return getTextWithout_N_FirstWords(spokenText, 2);
        if(isTaskNotDone(spokenText)) return getTextWithout_N_FirstWords(spokenText, 3);
        else return getTextWithout_N_FirstWords(spokenText, 1);
    }
    // TODO: Fix bad sentences (Chcck if spokenText is to short and does overflow out of bounds)
    @Override
    public boolean isSetTitle(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return isSetY(spokenText, "titulo");
    }

    @Override
    public boolean isSetDescription(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return isSetY(spokenText, "descripcion");
    }

    private boolean isSetY(String spokenText, String Y){
        return isXY(spokenText, "asignar", Y);
    }
    private boolean isXY(String spokenText, String X, String Y) {
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size()>= 3 && words.get(0).equals(X) && words.get(1).equals(Y);
    }

    @Override
    public String getTitleFromText(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return getTextWithout_N_FirstWords(spokenText, 2);
    }

    @Override
    public String getDescriptionFromText(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return getTextWithout_N_FirstWords(spokenText, 2);
    }

    @Override
    public boolean isSave(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 1 && words.get(0).equals("guardar");
    }

    @Override
    public boolean isConfirmation(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 1 && words.get(0).equals("si");
    }

    /**
     *
     * @param spokenText
     * @param N Numbers of words that will be skipped starting from first elemnent
     * @return
     */
    private String getTextWithout_N_FirstWords(String spokenText, int N) {
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        words = words.subList(N, words.size());
        return String.join(" ", words);
    }

    @Override
    public String normalizeText(String text) {
        StringBuilder returnText = new StringBuilder();
        text = text.toLowerCase();
        for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if(c == 'á') c = 'a';
            if(c == 'é') c = 'e';
            if(c == 'í') c = 'i';
            if(c == 'ó') c = 'o';
            if(c == 'ú') c = 'u';
            returnText.append(c);
        }
        return returnText.toString();
    }



    //************************** SHOPPING LIST *****************************//
    @Override
    public boolean isShoppingList(String spokenText) {
        return spokenText.equals("lista") || spokenText.equals("listas");
    }

    @Override
    public boolean isCreate(String spokenText) { // crear [title]
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("crear");
    }

    @Override
    public boolean isUpdate(String spokenText) { // modificar [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("modificar");
    }

    @Override
    public boolean isDelete(String spokenText) { // eliminar [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return !isDeleteAll(spokenText) && words.size() >= 2 && words.get(0).equals("eliminar");
    }

    @Override
    public boolean isDeleteAll(String spokenText) { // eliminar todo
        spokenText = this.normalizeText(spokenText);
        return spokenText.equals("eliminar todo");
    }

    @Override
    public boolean isSeeElement(String spokenText) { // ver [title]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("ver");
    }

    @Override
    public String getIdFromText(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        if(isTaskDone(spokenText)) return getTextWithout_N_FirstWords(spokenText, 2);
        if(isTaskNotDone(spokenText)) return getTextWithout_N_FirstWords(spokenText, 3);
        else return getTextWithout_N_FirstWords(spokenText, 1);
    }

    @Override
    public boolean isBought(String spokenText) { // comprar [element]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("marcar");
    }

    @Override
    public boolean isNotBought(String spokenText) { // no comprar [element]
        spokenText = this.normalizeText(spokenText);
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.size() >= 2 && words.get(0).equals("desmarcar");
    }



    //************************** EVENTOS *****************************//
    @Override
    public boolean isGoToEvents(String spokenText) {
        return spokenText.equals("evento") || spokenText.equals("eventos");
    }

    @Override
    public boolean isSetYear(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return isSetY(spokenText, "año");
    }

    @Override
    public boolean isSetMonth(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return isSetY(spokenText, "mes");
    }

    @Override
    public boolean isSetDay(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return isSetY(spokenText, "dia");
    }

    @Override
    public String getDateElementFromText(String spokenText) {
        spokenText = this.normalizeText(spokenText);
        return getTextWithout_N_FirstWords(spokenText, 2);
    }

}
