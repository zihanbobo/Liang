function controlExLevel(level){if(level == "exp"){zTree.expandAll(true);}else if(level == "col"){zTree.expandAll(false);}else{var nodes = zTree.getNodes();for (var i = 0; i < nodes.length; i++) {if(nodes[i].level < level){zTree.expandNode(nodes[i], true, null, null);}}}}