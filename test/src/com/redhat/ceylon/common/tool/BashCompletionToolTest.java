package com.redhat.ceylon.common.tool;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class BashCompletionToolTest {
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    private PrintStream savedOut;
    private ByteArrayOutputStream out;
    
    Iterable<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    
    public void redirectStdout() {
        this.savedOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }
    
    public void restoreStdout() {
        System.setOut(this.savedOut);
    }
    
    @Test
    public void testPlumbing()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertTrue(model.isPlumbing());
    }
    
    @Test
    public void testToolNameCompletion()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        ""));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "example\n" +
        		"minimums\n" +
        		"", new String(out.toByteArray()));
    }
    
    @Test
    public void testToolNameCompletion_partial()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        "e"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals("example \n", new String(out.toByteArray()));
    }
    
    @Test
    public void testOptionNameCompletion()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "--file\\=\n" +
                "--list-option\\=\n"+
                "--long-name\n"+
                "--pure-option\n"+
                "--short-name\\=\n"+
                "--thread-state\\=\n"+
                "", new String(out.toByteArray()));
    }
    
    @Test
    public void testOptionNameCompletion_partial()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--l"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "--list-option\\=\n" +
                "--long-name\n" +
                "", new String(out.toByteArray()));
    }
    
    @Test
    public void testFileCompletion()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file="));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--file=src/\n"));
        Assert.assertTrue(files, files.contains("--file=test/\n"));
    }
    
    @Test
    public void testFileCompletion_partial()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file=s"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--file=src/ \n"));
        Assert.assertFalse(files, files.contains("--file=test/ \n"));
    }
    
    @Test
    public void testEnumCompletion()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state="));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--thread-state=NEW\n"));
        Assert.assertTrue(files, files.contains("--thread-state=BLOCKED\n"));
        Assert.assertTrue(files, files.contains("--thread-state=RUNNABLE\n"));
    }
    
    @Test
    public void testEnumCompletion_partial()  throws Exception {
        PluginModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state=N"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--thread-state=NEW \n"));
        Assert.assertFalse(files, files.contains("--thread-state=BLOCKED \n"));
        Assert.assertFalse(files, files.contains("--thread-state=RUNNABLE \n"));
    }

}
