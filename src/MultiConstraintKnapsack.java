import java.io.File;  // Import the File class
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MultiConstraintKnapsack {

	public static void main(String[] args) throws IOException {
		
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		System.out.println("Enter txt name : (with .txt)" );
		String txtName = myObj.nextLine();  // Read user input
		
		File myObject = new File(txtName);
		Scanner sc = new Scanner(myObject);
		
		int knapsackCount = sc.nextInt();
		int itemCount = sc.nextInt();
		int values[] = new int[itemCount];
		
		for (int i = 0; i < itemCount; i++) {
			values[i] = sc.nextInt();
		}
		
		int knapsackCapacities[] = new int[knapsackCount];
		int tempKnapsackCapacities[] = new int[knapsackCount];
		
		for (int i = 0; i < knapsackCount; i++) {
			knapsackCapacities[i] = sc.nextInt();
			tempKnapsackCapacities[i] = knapsackCapacities[i];
		}
		
		ArrayList<int[]> knapsackWeightsArrays = new ArrayList<int[]>();
		
		for (int i = 0; i < knapsackCount; i++) {
			int[] knapsackWeights = new int[itemCount];
			
			for (int j = 0; j < itemCount; j++) {
				knapsackWeights[j] = sc.nextInt();
			}
			knapsackWeightsArrays.add(knapsackWeights);
		}
		
		ArrayList<Item[]> ValuePerWeightArrays = new ArrayList<Item[]>();
		ArrayList<Item[]> UnsortedItemArrays = new ArrayList<Item[]>();
		
		for (int i = 0; i < knapsackCount; i++) {
			Item itemArray[] = new Item[itemCount];
			Item itemList[] = new Item[itemCount];
			
			for (int j = 0; j < itemCount; j++) {
				Item item = new Item();
				item.index = j;
				item.value = values[j];
				if (knapsackWeightsArrays.get(i)[j] == 0) {
					item.valuePerWeight = 999999;
				}
				else {
					item.valuePerWeight = (double)values[j] / (knapsackCount * knapsackCount + knapsackWeightsArrays.get(i)[j]);
				}
				itemArray[j] = item;
				itemList[j] = item;
			}
			
			UnsortedItemArrays.add(itemList);
			ValuePerWeightArrays.add(itemArray);
		}
		
		
		for (int i = 0; i < knapsackCount; i++) {
			for (int j = 0; j < itemCount; j++) {
				ValuePerWeightArrays.get(i)[j].constraint = knapsackWeightsArrays.get(i)[j];
			}
		}
		
		
		for (int i = 0; i < knapsackCount; i++) {
			SortArray(ValuePerWeightArrays.get(i));
		}
		
		ArrayList<int[]> resultBinaryArrays = new ArrayList<int[]>();
		
		
		for (int i = 0; i < knapsackCount; i++) {
			int resultBinaryArray[] = new int[itemCount];
			
			for (int j = itemCount-1; j >= 0; j--) {
				if (ValuePerWeightArrays.get(i)[j].constraint < tempKnapsackCapacities[i]) {
					resultBinaryArray[ValuePerWeightArrays.get(i)[j].index] = 1;
					tempKnapsackCapacities[i] -= ValuePerWeightArrays.get(i)[j].constraint;
				}
			}
			resultBinaryArrays.add(resultBinaryArray);
		}
		
		
		int[] temp = new int[itemCount];
		
		for (int i = 0; i < itemCount; i++) {
			temp[i] = resultBinaryArrays.get(0)[i];
		}
		
		for (int i = 0; i < knapsackCount - 1; i++) {
			for (int j = 0; j < itemCount; j++) {
				if (temp[j] == resultBinaryArrays.get(i+1)[j] && resultBinaryArrays.get(i+1)[j] == 1) {
					temp[j] = 1;
				}
				else {
					temp[j] = 0;
				}
			}
		}
		
		
		
		for (int i = 0; i < knapsackCount; i++) {
			tempKnapsackCapacities[i] = knapsackCapacities[i];
		}
		
		for (int i = 0; i < knapsackCount; i++) {
			for (int j = 0; j < itemCount; j++) {
				if (temp[j] == 1) {
					tempKnapsackCapacities[i] -= UnsortedItemArrays.get(i)[j].constraint;
				}
			}
		}

		int minimum = tempKnapsackCapacities[0];
		int minIndex = 0;
		
		
		for (int i = 1; i < knapsackCount; i++) {
			if (tempKnapsackCapacities[i] < minimum) {
				minimum = tempKnapsackCapacities[i];
				minIndex = i;
			}
		}
		
		boolean checkConstraint = false;
		
			for (int j = itemCount-1; j >= 0; j--) {
				for (int i = 0; i < knapsackCount; i++) {
					if (UnsortedItemArrays.get(i)[ValuePerWeightArrays.get(minIndex)[j].index].constraint > tempKnapsackCapacities[i]) {
						checkConstraint = false;
						break;
					}	
					else {
						checkConstraint = true;
					}
				}
				
				if (checkConstraint) {
					if (temp[ValuePerWeightArrays.get(minIndex)[j].index] != 1) {
						temp[ValuePerWeightArrays.get(minIndex)[j].index] = 1;
						for (int a = 0; a < knapsackCount; a++) {
							tempKnapsackCapacities[a] -= UnsortedItemArrays.get(a)[ValuePerWeightArrays.get(minIndex)[j].index].constraint;
						}
					}
				}
			}

			int result = 0;
			
			for (int j = 0; j < 1; j++) {
				for (int i = 0; i < itemCount; i++) {
					if (temp[i] == 1) {
						result += values[i];
					}
				}
			}
			
		
		sc.close();
			
			sc = new Scanner(System.in);
			System.out.println("Enter output txt name : (with .txt)");
			txtName = sc.nextLine();  // Read user input
			
			File outputFile = new File(txtName);
			
			if (outputFile.createNewFile()) {
		    	  
				System.out.println("File created: " + outputFile.getName());
		    } 
		    
		    else {
		    	System.out.println("File already exists.");
		    }
			
			PrintWriter printWriter = new PrintWriter(txtName);
		    printWriter.println(result);
			
			for (int i = 0; i < itemCount; i++) {
				if (i == itemCount - 1) {
					printWriter.print(temp[i]);
				}
				else {
					printWriter.println(temp[i]);
				}
			}
			
			printWriter.close();
			
	} //main bracket
	
	public static void SortArray(Item[] itemArray) {
		
		Item temp;
		
		for (int i = 1; i < itemArray.length; i++) {
		    for (int j = i; j > 0; j--) {
			     if (itemArray[j].valuePerWeight < itemArray[j - 1].valuePerWeight) {
			      temp = itemArray[j];
			      itemArray[j] = itemArray[j - 1];
			      itemArray[j - 1] = temp;
			     }
		    }
		}
		
	}
} // class bracket