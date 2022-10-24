import BaseClass from "../util/baseClass.js";
import DataStore from "../util/DataStore.js";
import CustomerClient from "../api/customerClient.js";


class CustomerPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetClothingById', 'onGetClothingByType', 'renderBusiness'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        let searchIdButton = document.getElementById('search-id-button')
        searchIdButton.addEventListener('click', this.onGetClothingById)

        let searchTypeButton = document.getElementById('search-type-button')
        searchTypeButton.addEventListener('click', this.onGetClothingByType)

        this.client = new CustomerClient();

        this.dataStore.addChangeListener(this.renderBusiness)
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

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetClothingByType(event) {
    // Prevent the page from refreshing on form submit
            event.preventDefault();

            let itemName = document.getElementById("item-name-field").value;
            this.dataStore.set("clothing", null);

            let result = await this.client.onGetClothingByType(itemName, this.errorHandler);
            this.dataStore.set("clothing", result);
            if (result) {
                this.showMessage(`Got ${result.itemName}!`)
            } else {
                this.errorHandler("Error doing GET!  Try again...");
            }
    }

    async onGetClothingById(event) {
        // Prevent the page from refreshing on form submit
                event.preventDefault();

                let clothingId = document.getElementById("item-id-field").value;
                this.dataStore.set("clothing", null);

                let result = await this.client.onGetClothingById(clothingId, this.errorHandler);
                this.dataStore.set("clothing", result);
                if (result) {
                    this.showMessage(`Got ${result.clothingId}!`)
                } else {
                    this.errorHandler("Error doing GET!  Try again...");
                }
        }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const customerPage = new CustomerPage();
    customerPage.mount();
};

window.addEventListener('DOMContentLoaded', main);