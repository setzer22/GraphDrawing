from textx.metamodel import metamodel_from_file
from textx.export import metamodel_export, model_export
from models import MLayoutGraph, MLayoutSubgraphs
import os

class Interpreter():

    def __init__(self):
        meta_model_path = os.path.join(os.getcwd(), 'src\language\layout.tx')
        print meta_model_path
        metamodel = metamodel_from_file(meta_model_path)
        self.metamodel = metamodel

    def execute(self, model_str):
	
        model = self.metamodel.model_from_str(model_str)
        
        if model.__class__.__name__ == 'LayoutGraph':
                layoutGraph = Interpreter.execute_one(model.layoutType, 'graph')
                return layoutGraph
        else:
            subgraphs = []
            for layoutSubgraph in model.layoutSubgraphs:
                   
                subgraph = layoutSubgraph.subgraph
                if subgraph == None:
                   graph = 'others'
                else:
                    vertices = ''
                    for i,vertex in enumerate(subgraph.vertices):
                        vertices = vertices + str(vertex.index)
                        if i < len(subgraph.vertices) - 1:
                             vertices = vertices + ','
                    graph = vertices
                   
                layoutType = layoutSubgraph.layoutType
                layoutOneSubgraph = Interpreter.execute_one(layoutType, graph)
              
                subgraphs.append(layoutOneSubgraph)  
            
            return MLayoutSubgraphs(subgraphs)
        
        return 'executed'
    
    @staticmethod
    def execute_one(layout, graph):
            layoutType = layout.howToLayout
            
            if layoutType== 'algorithm':
                #a map that will contain all information about the algorithm
                algorithmProperties = {}
                algorithm = layout.algorithm
                #the algorithm could be of numerous classes
                for attr, value in algorithm.__dict__.iteritems():
                    if not (attr.startswith('_') or attr == 'parent'):
                        algorithmProperties[attr] = value
                      
                layoutGraph =  MLayoutGraph(graph = graph, type = layoutType, algorithm =  algorithmProperties)  
                return layoutGraph
            elif layoutType == 'style':
                style = layout.style
                layoutGraph =  MLayoutGraph(graph = graph, type = layoutType, style = style)
                return layoutGraph
            elif layoutType == 'criteria':
                criteriaList = [];
                criteria = layout.aestheticCriteria
                for criterion in criteria:
                    criterionProperties = {}
                    for attr, value in criterion.__dict__.iteritems():
                        if not (attr.startswith('_') or attr == 'parent'):
                            criterionProperties[attr] = value
                    criteriaList.append(criterionProperties)
                layoutGraph = MLayoutGraph(graph = graph, type = layoutType, aestheticCriteria = criteriaList)
                return layoutGraph