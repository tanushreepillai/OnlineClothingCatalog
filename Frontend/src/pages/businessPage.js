import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import BusinessClient from "../api/businessClient";
import CustomerClient from "../api/customerClient.js";


class BusinessPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onGetClothing', 'onUpdate', 'onDelete','onGetClothingById', 'onGetClothingByType', 'renderBusiness', 'renderCustomer', 'renderItemNameSearch'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        let addClothingButton = document.getElementById('add-clothing-button')
        addClothingButton.addEventListener('click', this.onCreate)

        let deleteClothingButton = document.getElementById('delete-clothing-button')
        deleteClothingButton.addEventListener('click', this.onDelete)

        let updateClothingButton = document.getElementById('update-clothing-button')
        updateClothingButton.addEventListener('click', this.onUpdate)

        let searchIdButton = document.getElementById('search-id-button')
        searchIdButton.addEventListener('click', this.onGetClothingById)

        let searchTypeButton = document.getElementById('search-type-button')
        searchTypeButton.addEventListener('click', this.onGetClothingByType)

        this.client = new BusinessClient();


//        this.dataStore.addChangeListener(this.renderBusiness)
//        this.dataStore.addChangeListener(this.renderCustomer)
//        this.dataStore.addChangeListener(this.renderItemNameSearch)


//        this.onGetClothing()
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderBusiness() {
            let resultArea = document.getElementById("result-info");

            const clothingList = this.dataStore.get("clothing");
            let content = "";

            if (clothingList) {
                content += "<ul>"
                for(let clothing of clothingList) {
                    content += `<li>
                        <h3>${clothing.clothingId}</h3>
                        <h4>${clothing.itemName}</h4>
                        <p>${clothing.inStock}</p>
                        <p>${clothing.price}</p>
                        <p>${clothing.itemSize}</p>
                        </li>
                        `
                }
                    content += "</ul>"
                    resultArea.innerHTML = content;
            } else {
                resultArea.innerHTML = "No Item";
            }
    }

        async renderItemNameSearch() {
                let resultArea = document.getElementById("customer-itemName-result-info");

                const clothingList = this.dataStore.get("clothing");
                let content = "";

                if (clothingList) {
                    content += "<ul>"
                    for(let clothing of clothingList) {
                        content += `<li>
                            <h3>${clothing.clothingId}</h3>
                            <h4>${clothing.itemName}</h4>
                            <p>${clothing.inStock}</p>
                            <p>${clothing.price}</p>
                            <p>${clothing.itemSize}</p>
                            </li>
                            `
                    }
                        content += "</ul>"
                        resultArea.innerHTML = content;
                } else {
                    resultArea.innerHTML = "No Item";
                }
        }

    async renderCustomer() {
            let resultArea = document.getElementById("customer-result-info");

            const clothing = this.dataStore.get("clothing");

            if (clothing) {
                resultArea.innerHTML = `
                    <h3>${clothing.clothingId}</h3>
                    <h4>${clothing.itemName}</h4>
                    <p>${clothing.inStock}</p>
                    <p>${clothing.price}</p>
                    <p>${clothing.itemSize}</p>
                `
            } else {
                resultArea.innerHTML = "No Item";
            }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onGetClothingByType(event) {
    // Prevent the page from refreshing on form submit
       event.preventDefault();

       let itemName = document.getElementById("item-name-field").value;
       //this.dataStore.set("clothing", null);
        let result = await this.client.getAllClothingSpecificType(itemName, this.errorHandler);
        await this.dataStore.set("clothing", result);
        if (result) {
          this.showMessage(`Got item(s)`)
      } else {
          this.errorHandler("Error doing GET!  Try again...");
        }
        this.renderItemNameSearch()
   }

   async onGetClothingById(event) {
               // Prevent the page from refreshing on form submit
        event.preventDefault();

        let clothingId = document.getElementById("item-id-field").value;
        let result = await this.client.getClothingById(clothingId, this.errorHandler);
        await this.dataStore.set("clothing", result);
        if (result) {
          this.showMessage(`Got ${result.clothingId}!`)
        } else {
          this.errorHandler("Error doing GET!  Try again...");
        }
        this.renderCustomer()
      }


   async onGetClothing(event) {
        let result = await this.client.getAllClothing(this.errorHandler);
        this.dataStore.set("clothing", result);
    }

    async onCreate(event) {
            // Prevent the page from refreshing on form submit
            event.preventDefault();

            let clothingId = document.getElementById("clothing-id").value;
            let itemName = document.getElementById("item-name").value;
            let inStock = document.getElementById("in-stock").value;
            let price = document.getElementById("price").value;
            let itemSize = document.getElementById("item-size").value;

            const addedClothing = await this.client.addClothing(clothingId,
                itemName,
                inStock,
                price,
                itemSize,
                this.errorHandler);

            if (addedClothing) {
                this.showMessage("Clothing added")
            } else {
                this.errorHandler("Error adding!  Try again...");
            }
           await this.onGetClothing()
           this.renderBusiness()
        }


    async onUpdate(event) {
            // Prevent the page from refreshing on form submit
            event.preventDefault();

            let clothingId = document.getElementById("update-clothing-id").value;
            let itemName = document.getElementById("update-item-name").value;
            let inStock = document.getElementById("update-in-stock").value;
            let price = document.getElementById("update-price").value;
            let itemSize = document.getElementById("update-item-size").value;

            const updatedClothing = await this.client.updateClothing(clothingId,
                itemName,
                inStock,
                price,
                itemSize,
                this.errorHandler);

            if (updatedClothing) {
                this.showMessage("Clothing updated")
            } else {
                this.errorHandler("Error updating!  Try again...");
            }
           await this.onGetClothing()
           this.renderBusiness()
    }


    async onDelete(event) {
            // Prevent the page from refreshing on form submit
            event.preventDefault();

            let clothingId = document.getElementById("delete-clothing-id").value;
            if (clothingId) {
            await this.client.deleteClothing(clothingId, this.errorHandler);
                this.showMessage("Clothing deleted");
            } else {
                this.errorHandler("Error deleting!  Try again...");
            }
           await this.onGetClothing()
           this.renderBusiness()
        }


}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const businessPage = new BusinessPage();
    businessPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
