package com.example.myapplication.NaturalLanguageProcessing;

import java.util.Arrays;
import java.util.List;

public class NaturalLanguageProcessingImpl implements NaturalLanguageProcessing {

    @Override
    public boolean isCreateTask(String spokenText) { // CREATE TASK [title]
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals("create");
    }

    @Override
    public boolean isDeleteTask(String spokenText) { // DELETE TASK [title]
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals("delete");
    }

    @Override
    public boolean isUpdateTask(String spokenText) { // UPDATE TASK [title]
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals("update");
    }

    @Override
    public boolean isGoToTask(String spokenText) { // TASKS
        return spokenText.equals("task") || spokenText.equals("tasks") ||  spokenText.equals("ask") || spokenText.equals("as");
    }

    @Override
    public boolean isTaskNotDone(String spokenText) {
        return false;
    }

    @Override
    public boolean isTaskDone(String spokenText) {
        return false;
    }

    /**
     *
     * @param spokenText The spokenText is a valid text with spokenText.split(" ").lenght >= 3
     * @return Returns the taskID
     */
    @Override
    public String getTaskIdFromText(String spokenText) { // TODO: Alternatively use the position in the list to identify the task
        List<String> words = Arrays.asList(spokenText.split(" "));
        words = words.subList(2, words.size());
        String taskId = String.join(" ", words);
        return taskId;
    }
    // TODO: Fix bad sentences (Chcck if spokenText is to short and does overflow out of bounds)
    @Override
    public boolean isSetTitle(String spokenText) {
        return isSetY(spokenText, "title");
    }

    @Override
    public boolean isSetDescription(String spokenText) {
        return isSetY(spokenText, "description");
    }

    private boolean isSetY(String spokenText, String Y){
        return isXY(spokenText, "set", Y);
    }
    private boolean isXY(String spokenText, String X, String Y) {
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals(X) && words.get(1).equals(Y);
    }

    @Override
    public String getTitleFromText(String spokenText) {
        return getTextWithout_N_FirstWords(spokenText, 2);
    }

    @Override
    public String getDescriptionFromText(String spokenText) {
        return getTextWithout_N_FirstWords(spokenText, 2);
    }

    @Override
    public boolean isSave(String spokenText) {
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals("save") || words.get(0).equals("safe")  || words.get(0).equals("CEIP") || words.get(0).equals("seis");
    }

    @Override
    public boolean isConfirmation(String spokenText) {
        List<String> words = Arrays.asList(spokenText.split(" "));
        return words.get(0).equals("yes") || words.get(0).equals("confirm");
    }

    @Override
    public String normalizeText(String text) {
        return text;
    }

    /**
     *
     * @param spokenText
     * @param N Numbers of words that will be skipped starting from first elemnent
     * @return
     */
    private String getTextWithout_N_FirstWords(String spokenText, int N) {
        List<String> words = Arrays.asList(spokenText.split(" "));
        words = words.subList(N, words.size());
        return String.join(" ", words);
    }

    @Override
    public boolean isDeleteAllTask(String spokenText) {
        return spokenText.equals("delete all");
    }

    @Override
    public boolean isShoppingList(String spokenText) {
        return false;
    }

    @Override
    public boolean isCreate(String spokenText) {
        return false;
    }

    @Override
    public boolean isUpdate(String spokenText) {
        return false;
    }

    @Override
    public boolean isDelete(String spokenText) {
        return false;
    }

    @Override
    public boolean isDeleteAll(String spokenText) {
        return false;
    }

    @Override
    public boolean isSeeElement(String spokenText) {
        return false;
    }

    @Override
    public String getIdFromText(String spokenText) {
        return null;
    }


}
