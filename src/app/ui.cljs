(ns app.ui
  (:require ["@mui/material" :as mui]
            ["@mui/material/styles" :as styles]))


(defn create-theme [theme]
  (styles/createTheme (clj->js theme)))

(def dark-theme
  (create-theme {:palette {:mode :dark}}))

(defn theme-provider [theme child]
  [:> styles/ThemeProvider {:theme theme} [:> mui/CssBaseline] child])


(defn button [props & children]
  (into [:> mui/Button props] children))