/**
 * Copyright 2023 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.coral.vis;

import java.io.File;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.util.SqlVisitor;

import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;


public class VisualizationUtil {
  private final File outputDirectory;

  private VisualizationUtil(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * Create a visualization util.
   * @param outputDirectory directory to write the visualization files to.
   * @return a visualization util.
   */
  public static VisualizationUtil create(File outputDirectory) {
    return new VisualizationUtil(outputDirectory);
  }

  /**
   * Visualize the coral sql node to a file.
   * @param sqlNode root of the SqlNode tree to visualize.
   * @param fileName name of the file to write the visualization to.
   */
  public void visualizeCoralSqlNodeToFile(SqlNode sqlNode, String fileName) {
    SqlVisitor<Node> sqlVisitor = new SqlNodeVisualizationVisitor();
    Node node = sqlNode.accept(sqlVisitor);
    Graph graph =
        Factory.graph("test").directed().nodeAttr().with(Font.size(10)).linkAttr().with(Font.size(10)).with(node);
    File outputFile = new File(outputDirectory, fileName);
    try {
      Graphviz.fromGraph(graph).width(1000).render(Format.PNG).toFile(outputFile);
    } catch (Exception e) {
      throw new RuntimeException("Could not render graphviz file:" + e);
    }
  }
}
